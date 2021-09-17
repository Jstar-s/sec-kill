package com.tstar.seckill;

import com.tstar.seckill.utils.DBUtil;
import com.tstar.seckill.utils.MysqlMsg;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.security.interfaces.ECKey;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class SecKillApplicationTests {

    @Autowired
    private DBUtil dbUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
    }



    @Test
    public  void test2() throws SQLException, ClassNotFoundException {
        Connection conn = dbUtil.getConn();
        System.out.println(conn);
    }

    @Test
    public void testLock01() {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        //占位，如果key不存在才可以设置成功
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1");


        //如果占位成功，进行正常操作
        if (isLock) {
            valueOperations.set("name", "xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name :" + name);
            redisTemplate.delete("k1");
        } else {
            System.out.println("有线程在使用，稍后再试");
        }
    }

}
