package com.atguigu.oss.controller;

import com.atguigu.commonutils.ResultEntity;
import com.atguigu.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
//@CrossOrigin
public class OssController {

    @Autowired
    private OssService ossService;

    // 上传头像方法
    @PostMapping
    public ResultEntity uploadOssFile(MultipartFile file) {
        // 获取上传的文件地址
        String url = ossService.uploadFileAvatar(file);
        return ResultEntity.ok().data("url", url);
    }

}
