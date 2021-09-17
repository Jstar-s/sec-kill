package com.tstar.seckill.vo;

import com.sun.tools.corba.se.idl.constExpr.Or;
import com.tstar.seckill.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jstar
 * @version 1.0
 * @date 2021/8/1 2:36 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo  {

    private Order order;
    private GoodsVo goodsVo;


}
