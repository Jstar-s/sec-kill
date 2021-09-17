package com.tstar.seckill.controller;


import com.tstar.seckill.pojo.User;
import com.tstar.seckill.service.IOrderService;
import com.tstar.seckill.vo.OrderDetailVo;
import com.tstar.seckill.vo.RespBean;
import com.tstar.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jstar
 * @since 2021-07-30
 */
@Controller
@RequestMapping("/order")
public class OrderController {


    @Autowired
    IOrderService orderService;

    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user, Long orderId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo  detail = orderService.detail(orderId);
        return RespBean.success(detail);
    }

}
