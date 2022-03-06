package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author peanut
 * @since 2022-03-01
 */
public interface PayLogService extends IService<PayLog> {

    Map createNative(String orderId);

    Map queryPayStatus(String orderId);

    void updateOrderStatus(Map<String,String> map);
}
