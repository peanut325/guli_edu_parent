package com.atguigu.vod.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    String uploadAliVideo(MultipartFile file);

    void deleteVideo(String videoId) throws ClientException;

    void deleteVideoBatch(List<String> videoIdList);

    String getPlayAuth(String videoId);
}
