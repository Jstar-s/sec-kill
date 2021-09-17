package com.tstar.seckill.config;

import com.tstar.seckill.pojo.User;

/**
 * @author Jstar
 * @version 1.0
 * @date 2021/8/7 2:36 下午
 */
public class UserContext {

    private static ThreadLocal<User> userHolder = new ThreadLocal<User>();

    public static  void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();

    }
}
