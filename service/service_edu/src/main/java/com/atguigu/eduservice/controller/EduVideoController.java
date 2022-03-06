package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.ResultEntity;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author peanut
 * @since 2022-02-18
 */
@RestController
@RequestMapping("/eduservice/edu-video")
//@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;

    // 新增小节
    @PostMapping("addVideo")
    public ResultEntity addVideo(@RequestBody EduVideo eduVideo) {
        boolean flag = eduVideoService.save(eduVideo);
        if (flag) {
            return ResultEntity.ok();
        } else {
            return ResultEntity.error();
        }
    }

    // 需要将视频也一起删除
    @DeleteMapping("deleteVideo/{id}")
    public ResultEntity deleteVideo(@PathVariable String id) {
        boolean flag = eduVideoService.removeEduVideo(id);
        if (flag) {
            return ResultEntity.ok();
        } else {
            return ResultEntity.error();
        }
    }

    // 根据chapterid查询小节
    @GetMapping("getVideoById/{id}")
    public ResultEntity getVideoById(@PathVariable String id) {
        EduVideo eduVideo = eduVideoService.getById(id);
        if (eduVideo != null) {
            return ResultEntity.ok().data("eduVideo", eduVideo);
        } else {
            return ResultEntity.error();
        }
    }

    // 修改小节
    @PutMapping("updateVideo")
    public ResultEntity updateVideo(@RequestBody EduVideo eduVideo) {
        boolean flag = eduVideoService.updateById(eduVideo);
        if (flag) {
            return ResultEntity.ok();
        } else {
            return ResultEntity.error();
        }
    }
}

