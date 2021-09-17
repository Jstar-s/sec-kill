package com.tstar.seckill.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Jstar
 * @version 1.0
 * @date 2021/7/31 1:49 下午
 */
@Data
@Component
@PropertySource("classpath:application.yml")
public class MysqlMsg {

    @Value("${spring.datasource.driver-class-name}")
    private String drive;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;
}
