package com.atguigu.vod.controller;

import com.atguigu.commonutils.ResultEntity;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.vod.service.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
//@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    // 上传视频
    @PostMapping("vodupload")
    public ResultEntity uploadVideo(MultipartFile file) {
        String videoId = vodService.uploadAliVideo(file);
        return ResultEntity.ok().data("videoId", videoId);
    }

    // 删除视频
    @DeleteMapping("deleteVideo/{videoId}")
    public ResultEntity deleteVideo(@PathVariable String videoId) {
        try {
            vodService.deleteVideo(videoId);
            return ResultEntity.ok().message("视频删除成功!");
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new GuliException(20001, "删除视频失败!");
        }
    }

    // 批量删除视频
    @DeleteMapping("deleteVideoBatch")
    public ResultEntity deleteVideoBatch(@RequestParam List<String> videoIdList) {
        try {
            vodService.deleteVideoBatch(videoIdList);
            return ResultEntity.ok().message("视频删除成功!");
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new GuliException(20001, "删除视频失败!");
        }
    }

    // 获取播放凭证
    @GetMapping("getPlayAuth/{videoId}")
    public ResultEntity getPlayAuth(@PathVariable String videoId) {
        String playAuth = vodService.getPlayAuth(videoId);
        return ResultEntity.ok().data("playAuth" , playAuth);
    }

}
