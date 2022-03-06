package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.ResultEntity;
import com.atguigu.orderservice.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author peanut
 * @since 2022-03-01
 */
@RestController
@RequestMapping("/orderservice/pay-log")
//@CrossOrigin
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    // 生成二维码
    @GetMapping("createNative/{orderId}")
    public ResultEntity createNative(@PathVariable String orderId) {
        Map map = payLogService.createNative(orderId);
        return ResultEntity.ok().data(map);
    }

    // 根据订单号查询支付状态
    @GetMapping("getOrderStatus/{orderId}")
    public ResultEntity getOrderStatus(@PathVariable String orderId) {
        // 获取返回的结果集
        Map map = payLogService.queryPayStatus(orderId);

        if (map == null) {
            return ResultEntity.error().message("支付失败!");
        }

        // 支付成功
        if (map.get("trade_state").equals("SUCCESS")) {
            // 修改订单的状态
            payLogService.updateOrderStatus(map);
            return ResultEntity.ok().message("支付成功!");
        }

        return ResultEntity.ok().code(25000).message("正在支付!");
    }
}

