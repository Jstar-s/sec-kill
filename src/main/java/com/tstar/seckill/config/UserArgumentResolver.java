package com.tstar.seckill.config;

import com.tstar.seckill.pojo.User;
import com.tstar.seckill.service.IUserService;
import com.tstar.seckill.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;

/**
 * 自定义用户参数
 * @author Jstar
 * @version 1.0
 * @date 2021/7/29 9:29 下午
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    IUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
//        System.out.println("userArgumentResolver");
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
//        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse response = nativeWebRequest.getNativeRequest(HttpServletResponse.class);
//        String ticket = CookieUtil.getCookieValue(request,"userTicket");
//        if (StringUtils.isEmpty(ticket)) {
//            return null;
//        }
//        return userService.getUserByCookie(ticket, request, response);
        return UserContext.getUser();
    }
}
