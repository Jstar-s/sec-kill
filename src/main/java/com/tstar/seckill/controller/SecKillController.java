package com.tstar.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tstar.seckill.pojo.Order;
import com.tstar.seckill.pojo.SeckillMessage;
import com.tstar.seckill.pojo.SeckillOrder;
import com.tstar.seckill.pojo.User;
import com.tstar.seckill.rabbitmq.MQSender;
import com.tstar.seckill.service.IGoodsService;
import com.tstar.seckill.service.IOrderService;
import com.tstar.seckill.service.ISeckillOrderService;
import com.tstar.seckill.utils.JsonUtil;
import com.tstar.seckill.vo.GoodsVo;
import com.tstar.seckill.vo.RespBean;
import com.tstar.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jstar
 * @version 1.0
 * @date 2021/7/30 6:27 下午
 */

@Controller
@RequestMapping("/secKill")
public class SecKillController implements InitializingBean {
    @Autowired
    IGoodsService goodsService;

    @Autowired
    ISeckillOrderService seckillOrderService;

    @Autowired
    IOrderService orderService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    MQSender mqSender;

    @Autowired
    private RedisScript<Long> script;

    private Map<Long, Boolean> EmptyStockMap = new HashMap<>();

    /**
     * 秒杀
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("/doSecKill")
    public String doSecKill(Model model, User user, Long goodsId) {
//        System.out.println("seckill");
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);

        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        System.out.println("goods" + goods);
        if (goods.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }

        // 判断是否重复抢购
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().
                eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
            return "secKillFail";
        }
        Order order =  orderService.secKill(user, goods);
//        System.out.println(order);
        model.addAttribute("orderInfo", order);
        model.addAttribute("goods", goods);
        return "orderDetail";
    }

    /**
     * 秒杀静态化
     * @param user
     * @return
     */
    @RequestMapping(value = "/{path}/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(@PathVariable String path, User user, Long goodsId) {
//        System.out.println("seckill");
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        boolean check = orderService.checkPath(user, goodsId, path);
        if (!check) {
            return RespBean.error(RespBeanEnum.PATH_ERROR);

        }
        // 通过redis获取, 判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        //内存标记，减少redis的访问
        if (EmptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();


        //预减库存， 保证操作的原子性
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        //通过lua脚本实现分布式锁
//        Long stock = (Long) redisTemplate.execute(script, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        System.out.println("stock" + stock);
        if (stock < 0)  {
            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.beanToString(seckillMessage));
        return RespBean.success(0);


        /**
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goods.getStockCount() < 1) {
//            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // 判断是否重复抢购
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().
//                eq("user_id", user.getId()).eq("goods_id", goodsId));

        // 通过redis获取, 判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goods.getId());
        if (seckillOrder != null) {
//            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        Order order =  orderService.secKill(user, goods);

         **/

    }


    /**
     * 获取秒杀结果
     * @throws Exception
     * @return orderId 成功 -1 秒杀失败 0 排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return  RespBean.success(orderId);
    }


    /**
     * 获取秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);

        }
        String str = orderService.createPath(user, goodsId);
        return RespBean.success(str);
    }



    /**
     * 系统初始化，把商品库存加载到redis
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(), false);
        }
        );
    }
}

