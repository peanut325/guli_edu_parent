package com.atguigu.eduservice.client;

import com.atguigu.commonutils.dto.LoginInfoDO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "service-ucenter", fallback = com.atguigu.eduservice.client.CommentDegradeFeignClient.class)     // 代理的微服务名
@Component
public interface CommentClient {

    // 调用ucenter查询登录信息，注意全路径
    @PostMapping("/ucenterservice/ucenter-member/getLoginInfoById/{memberId}")
    public LoginInfoDO getLoginInfoById(@PathVariable("memberId") String memberId);

}
