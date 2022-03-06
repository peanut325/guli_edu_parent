package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author peanut
 * @since 2022-03-01
 */
public interface OrderService extends IService<Order> {

    String saveOrder(String courseId, String memberIdByJwtToken);

    Order getOrderByOrderNo(String orderNo);

    int getOrderByCourseIdAndOrderId(String courseId, String memberId);
}
