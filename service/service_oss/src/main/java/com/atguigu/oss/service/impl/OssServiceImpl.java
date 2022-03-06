package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    @Override
    public String uploadFileAvatar(MultipartFile multipartFile) {

        //获取阿里云存储相关常量
        String endPoint = ConstantPropertiesUtil.END_POINT;
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;

        // 上传的url
        String uploadUrl = null;

        try {
            // 创建OSS实例
            OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

            // 获取上传文件流
            InputStream inputStream = multipartFile.getInputStream();

            // 构建日期路径：avatar/2019/02/26/文件名
            String datePath = new DateTime().toString("yyyy/MM/dd");

            // 文件名：uuid + 文件名
            String fileName = multipartFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            String newName = uuid + fileName;
            String fileUrl =  datePath + "/" + newName;

            // 文件上传至阿里云
            ossClient.putObject(bucketName, fileUrl, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            // 拼接返回的url地址
            uploadUrl = "https://" + bucketName + "." + endPoint + "/" + fileUrl;

            return uploadUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
