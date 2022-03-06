package com.atguigu.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.AliyunVodSDKUtils;
import com.atguigu.vod.utils.ConstantVodUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {

    @Override
    public String uploadAliVideo(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            // 截取除后缀的文件名
            String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));

            UploadStreamRequest request = new UploadStreamRequest(
                    ConstantVodUtils.ACCESS_KEY_ID,
                    ConstantVodUtils.ACCESS_KEY_SECRET,
                    title, originalFilename, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
            // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            String videoId = response.getVideoId();
            if (!response.isSuccess()) {
                String errorMessage = "阿里云上传错误：" + "code：" + response.getCode() + ", message：" + response.getMessage();
                if (StringUtils.isEmpty(videoId)) {
                    throw new GuliException(20001, errorMessage);
                }
            }
            return videoId;
        } catch (IOException e) {
            throw new GuliException(20001, "视频上传失败");
        }
    }

    @Override
    public void deleteVideo(String videoId) {
        try {
            // 获取client对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            // 获取请求对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(videoId);
            // 获取响应对象
            DeleteVideoResponse response = new DeleteVideoResponse();
            response = client.getAcsResponse(request);

        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001, "视频删除失败!");
        }

    }

    @Override
    public void deleteVideoBatch(List<String> videoIdList) {
        try {
            // 获取client对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            // 获取请求对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            // 将list中的多个id用 ，分隔开
            String videoIds = StringUtils.join(videoIdList.toArray(), ",");
            //支持传入多个视频ID，多个用逗号分隔，使用org.apache.commons.lang.StringUtils包
            request.setVideoIds(videoIds);
            // 获取响应对象
            DeleteVideoResponse response = new DeleteVideoResponse();
            response = client.getAcsResponse(request);

        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001, "视频删除失败!");
        }
    }

    // 获取视频播放凭证
    @Override
    public String getPlayAuth(String videoId) {
        //初始化客户端、请求对象和相应对象
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        try {
            //设置请求参数
            request.setVideoId(videoId);

            //获取请求响应
            response = client.getAcsResponse(request);

            //播放凭证
            return response.getPlayAuth();
        } catch (Exception e) {
            throw new GuliException(20001, "获取凭证失败!");
        }
    }
}

