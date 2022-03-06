package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.ResultEntity;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.vo.chapter.ChapterVO;
import com.atguigu.eduservice.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author peanut
 * @since 2022-02-18
 */
@RestController
@RequestMapping("/eduservice/edu-chapter")
//@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    // 根据课程id查询chapter
    @GetMapping("getChapterAll/{courseId}")
    public ResultEntity getChapterAll(@PathVariable String courseId) {
        List<ChapterVO> chapterVOList = eduChapterService.getChapterVO(courseId);
        return ResultEntity.ok().data("chapterList", chapterVOList);
    }

    // 根据章节id查询chapter
    @GetMapping("getChapterById/{chapterId}")
    public ResultEntity getChapterById(@PathVariable String chapterId) {
        EduChapter eduChapter = eduChapterService.getById(chapterId);
        return ResultEntity.ok().data("eduChapter", eduChapter);
    }

    // 添加章节
    @PostMapping("addChapter")
    public ResultEntity addChapter(@RequestBody EduChapter eduChapter) {
        eduChapterService.save(eduChapter);
        return ResultEntity.ok();
    }

    // 添加章节
    @PutMapping("updateChapter")
    public ResultEntity updateChapter(@RequestBody EduChapter eduChapter) {
        eduChapterService.updateById(eduChapter);
        return ResultEntity.ok();
    }

    // 根据章节id删除章节
    @DeleteMapping("deleteChapterById/{chapterId}")
    public ResultEntity deleteChapterById(@PathVariable String chapterId) {
        boolean flag = eduChapterService.deleteChapterById(chapterId);
        if (flag) {
            return ResultEntity.ok();
        } else {
            return ResultEntity.error();
        }
    }
}

