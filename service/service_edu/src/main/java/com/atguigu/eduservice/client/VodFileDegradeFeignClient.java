package com.atguigu.eduservice.client;

import com.atguigu.commonutils.ResultEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  熔断器
 */
@Component
public class VodFileDegradeFeignClient implements VodClient{
    @Override
    public ResultEntity deleteVideo(String videoId) {
        return ResultEntity.error().message("调用服务失败! time out");
    }

    @Override
    public ResultEntity deleteVideoBatch(List<String> videoIdList) {
        return ResultEntity.error().message("调用服务失败! time out");
    }
}
