package com.tstar.seckill.controller;


import com.tstar.seckill.pojo.User;
import com.tstar.seckill.rabbitmq.MQSender;
import com.tstar.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jstar
 * @since 2021-07-28
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    MQSender mqSender;


    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user) {
        System.out.println("info:" + user);
        return RespBean.success(user);
    }


//    /**
//     * 测试发送rabbitmq
//     */
//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq() {
//        mqSender.sent("hello");
//    }
//
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public void mq01() {
//        mqSender.sent("hello");
//    }
//
//
//    @RequestMapping("mq/direct01")
//    @ResponseBody
//    public void mq02() {
//        mqSender.send01("hello.red");
//
//    }
//
//
//    @RequestMapping("mq/direct02")
//    @ResponseBody
//    public void mq03() {
//        mqSender.send02("hello.green");
//    }
//
//    @RequestMapping("mq/topic01")
//    @ResponseBody
//    public void mq04() {
//        mqSender.send03("hello.red");
//    }
//
//    @RequestMapping("mq/topic02")
//    @ResponseBody
//    public void mq05() {
//        mqSender.send04("hello.green");
//    }

}
