package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.vo.chapter.ChapterVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author peanut
 * @since 2022-02-18
 */
    public interface EduChapterService extends IService<EduChapter> {

        List<ChapterVO> getChapterVO(String courseId);

        boolean deleteChapterById(String chapterId);
    }
