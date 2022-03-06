package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.ResultEntity;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author peanut
 * @since 2022-03-01
 */
@RestController
@RequestMapping("/orderservice/order")
//@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("saveOrder/{courseId}")
    public ResultEntity saveOrder(@PathVariable String courseId, HttpServletRequest request) {
        // 获取用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        String orderId = orderService.saveOrder(courseId, memberId);
        return ResultEntity.ok().data("orderId", orderId);
    }

    @GetMapping("getOrderInfoByOrederNo/{orderNo}")
    public ResultEntity getOrderInfoByOrderNo(@PathVariable String orderNo) {
        Order order = orderService.getOrderByOrderNo(orderNo);
        return ResultEntity.ok().data("order", order);
    }

    // 根据课程id和用户id查询订单
    @GetMapping("isBuy/{courseId}/{memberId}")
    public boolean isBuy(@PathVariable String courseId, @PathVariable String memberId) {
        int count = orderService.getOrderByCourseIdAndOrderId(courseId, memberId);
        return count > 0;
    }
}

