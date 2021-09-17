package com.tstar.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jstar
 * @version 1.0
 * @date 2021/8/4 1:30 下午
 */
@Configuration
public class RabbitTopicConfig {

//
//    private static final String QUEUE01= "queue_topic01";
//    private static final String QUEUE02= "queue_topic02";
//    private static final String EXCHANGE= "topicExchange";
//    private static final String ROUTINGKEY01= "#.queue.#";
//    private static final String ROUTINGKEY02= "*.queue.#";
//
//
//    @Bean
//    public Queue queue01() {
//        return new Queue(QUEUE01);
//    }
//
//    @Bean
//    public Queue queue02() {
//        return new Queue(QUEUE02);
//    }
//
//    @Bean
//    public TopicExchange topicExchange() {
//        return new TopicExchange(EXCHANGE);
//    }
//
//    @Bean
//    public Binding binding01() {
//        return BindingBuilder.bind(queue01()).to(topicExchange()).with(ROUTINGKEY01);
//    }
//
//    @Bean
//    public Binding binding02() {
//        return BindingBuilder.bind(queue02()).to(topicExchange()).with(ROUTINGKEY02);
//    }

    private static final String QUEUE = "seckillQueue";
    private static final String EXCHANGE = "seckillExchange";


    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }


    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
    }

}
