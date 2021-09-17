package com.tstar.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tstar.seckill.pojo.SeckillOrder;
import com.tstar.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jstar
 * @since 2021-07-30
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {


    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return
     */
    Long getResult(User user, Long goodsId);
}
