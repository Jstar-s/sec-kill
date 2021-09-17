package com.tstar.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tstar.seckill.pojo.Order;
import com.tstar.seckill.pojo.User;
import com.tstar.seckill.vo.GoodsVo;
import com.tstar.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jstar
 * @since 2021-07-30
 */
public interface IOrderService extends IService<Order> {


    /**
     * 秒杀
     * @param user
     * @param goods
     * @return
     */
    Order secKill(User user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);

    String createPath(User user, Long goodsId);

    boolean checkPath(User user, Long goodsId, String path);
}
