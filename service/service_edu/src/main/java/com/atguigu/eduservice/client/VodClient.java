package com.atguigu.eduservice.client;

import com.atguigu.commonutils.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-vod", fallback = com.atguigu.eduservice.client.VodFileDegradeFeignClient.class)     // 代理的微服务名
@Component
public interface VodClient {
    // 删除视频
    @DeleteMapping("/eduvod/video/deleteVideo/{videoId}")
    public ResultEntity deleteVideo(@PathVariable("videoId") String videoId);

    // 批量删除视频
    @DeleteMapping("/eduvod/video/deleteVideoBatch")
    public ResultEntity deleteVideoBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
