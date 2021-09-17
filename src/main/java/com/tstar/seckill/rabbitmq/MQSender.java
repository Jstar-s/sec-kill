package com.tstar.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jstar
 * @version 1.0
 * @date 2021/8/3 4:32 下午
 */

@Service
@Slf4j
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//
//    public void sent(Object msg) {
//        log.info("发送消息" + msg);
////        rabbitTemplate.convertAndSend("queue", msg);
//        rabbitTemplate.convertAndSend("fanoutExchange", "", msg);
//    }
//
//
//    public void send01(Object msg) {
//        log.info("发送red消息", msg);
//        rabbitTemplate.convertAndSend("directExchange", "queue.red", msg);
//    }
//
//
//
//    public void send02(Object msg) {
//        log.info("发送green消息", msg);
//        rabbitTemplate.convertAndSend("directExchange", "queue.green", msg);
//    }
//
//    public void send03(Object msg) {
//        log.info("send message send to queue01" + msg);
//        rabbitTemplate.convertAndSend("topicExchange", "queue.red.message", msg);
//    }
//
//
//    public void send04(Object msg) {
//        log.info("send message send to queue01and queue02" + msg);
//        rabbitTemplate.convertAndSend("topicExchange", "message.queue.green.abc", msg);
//    }

    /**
     * 发送秒杀信息
     * @param message
     */
    public void sendSeckillMessage(String message) {
        log.info("发送消息" + message);
        rabbitTemplate.convertAndSend("seckillExchange", "seckill.message", message);
    }




}
