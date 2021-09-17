package com.tstar.seckill.rabbitmq;

import com.tstar.seckill.pojo.SeckillMessage;
import com.tstar.seckill.pojo.SeckillOrder;
import com.tstar.seckill.pojo.User;
import com.tstar.seckill.service.IGoodsService;
import com.tstar.seckill.service.IOrderService;
import com.tstar.seckill.utils.JsonUtil;
import com.tstar.seckill.vo.GoodsVo;
import com.tstar.seckill.vo.RespBean;
import com.tstar.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Jstar
 * @version 1.0
 * @date 2021/8/3 4:35 下午
 */

@Service
@Slf4j
public class MQRecever {

    @Autowired
    RabbitTemplate rabbitTemplate;



//
//    @RabbitListener(queues = "queue")
//    public void receive(Object msg) {
//        log.info("接收消息" +  msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg) {
//        log.info("QUEUE01接收消息:" + msg);
//    }
//
//
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg) {
//        log.info("QUEUE02接收消息:" + msg);
//    }
//
//
//    @RabbitListener(queues = "queue_direct02")
//    public void receive04(Object msg) {
//        log.info("QUEUE02接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic01")
//    public void received05(Object msg) {
//        log.info("Queue01 received" + msg);
//    }
//
//
//    @RabbitListener(queues = "queue_topic02")
//    public void received06(Object msg) {
//        log.info("Queue02 received" + msg);
//    }

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    IOrderService orderService;

    /**
     * 下单操作
     */
    @RabbitListener(queues = "seckillQueue")
    public void received(String message) {
        log.info("接收的消息" + message);
        SeckillMessage seckillMessage = JsonUtil.stringToBean(message, SeckillMessage.class);
        long goodId = seckillMessage.getGoodId();
        User user = seckillMessage.getUser();
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodId);
        if (goodsVo.getStockCount() < 1) {
            return;
        }

        // 通过redis获取, 判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodId);
        if (seckillOrder != null) {
            return;
        }

        // 下单操作
        orderService.secKill(user, goodsVo);
    }

}

