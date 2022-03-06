package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.chapter.ChapterVO;
import com.atguigu.eduservice.entity.vo.chapter.VideoVO;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author peanut
 * @since 2022-02-18
 */
@Service
@Transactional(readOnly = true)
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVO> getChapterVO(String courseId) {
        // 根据courseId查询章节
        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id", courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(eduChapterQueryWrapper);

        // 根据courseId查询小节对象
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id", courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(eduVideoQueryWrapper);

        // 创建最终返回的VO集合
        ArrayList<ChapterVO> finalChapterVOList = new ArrayList<>();

        // 将查询出来的章节对象封装进VO对象
        for (EduChapter eduChapter : eduChapterList) {
            ChapterVO chapterVO = new ChapterVO();
            BeanUtils.copyProperties(eduChapter, chapterVO);

            for (EduVideo eduVideo : eduVideoList) {
                // 小节的chapterId相等进行封装
                if (eduChapter.getId().equals(eduVideo.getChapterId())) {
                    VideoVO videoVO = new VideoVO();
                    BeanUtils.copyProperties(eduVideo, videoVO);
                    // 将章节下的小节封装进对象的List集合属性
                    chapterVO.getChildren().add(videoVO);
                }
            }

            // 封装VO对象
            finalChapterVOList.add(chapterVO);
        }

        return finalChapterVOList;
    }

    @Override
    @Transactional(readOnly = false , rollbackFor = Exception.class , propagation = Propagation.REQUIRED)
    public boolean deleteChapterById(String chapterId) {
        // 先根据chapterId查询章节中是否有课时
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("chapter_id", chapterId);
        int count = eduVideoService.count(eduVideoQueryWrapper);

        if (count > 0) {    // 如果章节下有小节，则不进行删除
            throw new GuliException(20001, "删除失败，章节中有课时存在！");
        } else {     // 没有小节，进行删除
            int flag = baseMapper.deleteById(chapterId);
            // 删除成功即返回boolean类型
            return flag > 0;
        }
    }
}
