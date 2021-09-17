package com.tstar.seckill.utils;

import org.apache.coyote.OutputBuffer;

import java.util.UUID;

/**
 * @author Jstar
 * @version 1.0
 * @date 2021/7/29 2:52 下午
 */
public class UUIDUtill {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
