package com.atguigu.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-order", fallback = com.atguigu.eduservice.client.OrderDegradeFeignClient.class)
public interface OrderClient {
    // 根据课程id和用户id查询订单
    @GetMapping("/orderservice/order/isBuy/{courseId}/{memberId}")
    public boolean isBuy(@PathVariable("courseId") String courseId, @PathVariable("memberId") String memberId);

}
