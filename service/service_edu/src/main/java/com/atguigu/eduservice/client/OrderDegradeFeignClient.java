package com.atguigu.eduservice.client;

import org.springframework.stereotype.Component;

@Component
public class OrderDegradeFeignClient implements OrderClient{

    @Override
    public boolean isBuy(String courseId, String memberId) {
        return false;
    }
}
