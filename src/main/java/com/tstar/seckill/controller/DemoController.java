package com.tstar.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Jstar
 * @version 1.0
 * @date 2021/7/19 12:39 上午
 */

@Controller
@RequestMapping("/demo")
public class DemoController {

    /**
     * 测试页面跳转
     * @param model
     * @return
     */
    @RequestMapping("hello")
    public String hello(Model model) {
        model.addAttribute("name", "jstar");
        return "hello";
    }
}
