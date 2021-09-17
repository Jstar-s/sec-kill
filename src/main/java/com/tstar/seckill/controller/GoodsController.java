package com.tstar.seckill.controller;

import com.tstar.seckill.mapper.GoodsMapper;
import com.tstar.seckill.pojo.Goods;
import com.tstar.seckill.pojo.User;
import com.tstar.seckill.service.IGoodsService;
import com.tstar.seckill.service.IUserService;
import com.tstar.seckill.vo.DetailVo;
import com.tstar.seckill.vo.GoodsVo;
import com.tstar.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.WebConnection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Jstar
 * @version 1.0
 * @date 2021/7/29 3:12 下午
 */
@Controller
@RequestMapping("/goods")
@Slf4j
public class GoodsController {


    /**
     * 跳转商品页面
     * @param model
     * @param user
     * @return
     */

    @Autowired
    IUserService userService;

    @Autowired
    IGoodsService goodsService;

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;


/**
 *
 * 通过模版渲染页面返回前端
    @RequestMapping("/toList")
    public String toList( Model model, User user) {
//        log.info("{}", ticket);
//        if (StringUtils.isEmpty(ticket)) {
//            return  "login";
//        }
////        User user = (User) session.getAttribute(ticket);
//
//        User user = userService.getUserByCookie(ticket, request, response);
//        if (null == user) {
//            return "login";
//        }
//        System.out.println("user: "  + user);
        model.addAttribute("user", user);
//        Goods goods = goodsMapper.selectById("1");
//        System.out.println("begin");
//        List<GoodsVo> goodsVo = goodsMapper.findGoodsVo();
//        System.out.println("end");
//        for (GoodsVo vo : goodsVo) {
//            System.out.println(vo);
//        }
//        System.out.println(goods);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        return "goodsList";
    }
**/


    /**
     * 通过redis缓存页面，如果缓存不存,手动渲染返回并存入redis
     * @param model
     * @param user
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());

        //如果为空则手动渲染并存入redis数据库
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }



/**
    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail( Model model,User user, @PathVariable Long goodsId) {
//        System.out.println("toDetail");
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);

        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date now = new Date();

        //秒杀状态
        int secKillStatus = 0;
        int remainSeconds = 0;
        // 秒杀还未开始
        if (now.before(startDate)) {
            remainSeconds = (int) ((startDate.getTime() - now.getTime()) / 1000);

        } else if (now.after(endDate)) {
            //秒杀结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            secKillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("seckillStatus", secKillStatus);
        model.addAttribute("goods", goodsVo);
        return "goodsDetail";
    }
 **/

    /**
     * redis缓存页面
     * @param model
     * @param user
     * @param goodsId
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(value = "/toDetail/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail( Model model,User user, @PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail" + goodsId);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date now = new Date();

        //秒杀状态
        int secKillStatus = 0;
        int remainSeconds = 0;
        // 秒杀还未开始
        if (now.before(startDate)) {
            remainSeconds = (int) ((startDate.getTime() - now.getTime()) / 1000);

        } else if (now.after(endDate)) {
            //秒杀结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            secKillStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("seckillStatus", secKillStatus);
        model.addAttribute("goods", goodsVo);

        //如果为空则手动渲染并存入redis数据库
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsDetail" + goodsId, html, 60, TimeUnit.SECONDS);
        }
        return html;
    }


    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetailStatic( Model model,User user, @PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response) {

        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date now = new Date();

        //秒杀状态
        int secKillStatus = 0;
        int remainSeconds = 0;
        // 秒杀还未开始
        if (now.before(startDate)) {
            remainSeconds = (int) ((startDate.getTime() - now.getTime()) / 1000);

        } else if (now.after(endDate)) {
            //秒杀结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            secKillStatus = 1;
            remainSeconds = 0;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setSecKillStatus(secKillStatus);
        return RespBean.success(detailVo);
    }



}
