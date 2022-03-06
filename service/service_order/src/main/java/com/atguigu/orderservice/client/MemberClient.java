package com.atguigu.orderservice.client;

import com.atguigu.commonutils.dto.LoginInfoDO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter", fallback = com.atguigu.orderservice.client.MemberDegradeFeignClient.class)
public interface MemberClient {
    // 用户id获取用户信息
    @GetMapping("/ucenterservice/ucenter-member/getLoginInfoById/{memberId}")
    public LoginInfoDO getLoginInfoById(@PathVariable("memberId") String memberId);
}
