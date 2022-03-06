package com.atguigu.eduservice.client;

import com.atguigu.commonutils.dto.LoginInfoDO;
import org.springframework.stereotype.Component;

@Component
public class CommentDegradeFeignClient implements CommentClient {

    @Override
    public LoginInfoDO getLoginInfoById(String id) {
        return null;
    }
}
