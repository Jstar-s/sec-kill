package com.tstar.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tstar.seckill.pojo.Goods;
import com.tstar.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jstar
 * @since 2021-07-30
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
