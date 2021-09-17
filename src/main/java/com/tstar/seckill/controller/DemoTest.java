package com.tstar.seckill.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author Jstar
 * @version 1.0
 * @date 2021/7/31 2:17 下午
 */
@Controller
@RequestMapping("/demo")
@PropertySource("classpath:application.yml")
public class DemoTest {


    @Value("${spring.datasource.username}")
    public  String name;

    @Value("${spring.datasource.url}")
    public  String url;

    @RequestMapping("/get")
    @ResponseBody
    public String get(Model model) {
        System.out.println("获取项目名称为："+name);
        //获取自定义属性zidingyiUrl
        System.out.println("获取项目名称为："+ url);
        return "sss";
    }


}
