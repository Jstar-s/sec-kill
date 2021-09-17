package com.tstar.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tstar.seckill.pojo.Goods;
import com.tstar.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jstar
 * @since 2021-07-30
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();


    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
