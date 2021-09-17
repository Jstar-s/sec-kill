package com.tstar.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jstar
 * @version 1.0
 * @date 2021/8/4 2:23 下午
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessage {

    private User user;
    private long goodId;

}
