package com.atguigu.statisticsservice.client;

import com.atguigu.commonutils.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-ucenter")
@Component
public interface MemberClient {

    // 查询当天的注册人数
    @GetMapping("/ucenterservice/ucenter-member/countRegister/{day}")
    public ResultEntity countRegister(@PathVariable("day") String day);

}
