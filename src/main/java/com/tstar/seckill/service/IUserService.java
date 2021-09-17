package com.tstar.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tstar.seckill.pojo.User;
import com.tstar.seckill.vo.LoginVo;
import com.tstar.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jstar
 * @since 2021-07-28
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据cookie获取用户信息
     * @param userTicket
     * @return
     */
    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);


    RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response);
}
