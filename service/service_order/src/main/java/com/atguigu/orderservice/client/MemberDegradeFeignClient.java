package com.atguigu.orderservice.client;

import com.atguigu.commonutils.dto.LoginInfoDO;
import org.springframework.stereotype.Component;

@Component
public class MemberDegradeFeignClient implements MemberClient {
    @Override
    public LoginInfoDO getLoginInfoById(String memberId) {
        return null;
    }
}
