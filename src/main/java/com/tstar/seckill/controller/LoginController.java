package com.tstar.seckill.controller;

import com.tstar.seckill.service.IUserService;
import com.tstar.seckill.vo.LoginVo;
import com.tstar.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author Jstar
 * @version 1.0
 * @date 2021/7/28 10:39 下午
 */

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController
{

    /**
     * 跳转登录页面
     * @return
     */

    @Autowired
    private IUserService userService;

    @RequestMapping("/toLogin")
    public String toLogin() {
//        System.out.println("toLogin");
        return "login";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
//        log.info("{}", loginVo);

        return userService.doLogin(loginVo, request, response);
    }

}
