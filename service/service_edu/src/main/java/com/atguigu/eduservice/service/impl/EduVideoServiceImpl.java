package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author peanut
 * @since 2022-02-18
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;

    // 根据小节id删除视频
    @Override
    public boolean removeEduVideo(String id) {
        // 先查询课程下的章节是否存在视频id
        EduVideo eduVideo = baseMapper.selectById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        if (!StringUtils.isEmpty(videoSourceId)) {
            // 删除视频
            vodClient.deleteVideo(videoSourceId);
        }

        // 再将课时删除
        int flag = baseMapper.deleteById(id);

        return flag > 0;
    }

    // 根据课程id删除小节，并删除视频
    @Override
    public void removeEduVideoByCourseId(String courseId) {
        // 先查询courseId对应的小节
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id", courseId);
        // 只查询视频id的列
        eduVideoQueryWrapper.select("video_source_id");
        // 调用查询
        List<EduVideo> eduVideoList = baseMapper.selectList(eduVideoQueryWrapper);

        // 封装视频id的List集合
        ArrayList<String> videoIdList = new ArrayList<>();
        for (EduVideo eduVideo : eduVideoList) {
            String videoSourceId = eduVideo.getVideoSourceId();
            // 视频id不为空时
            if (!StringUtils.isEmpty(videoSourceId))
                videoIdList.add(videoSourceId);
        }

        // 判断videoIdList是否为空，为空则不调用删除视频的方法
        if (videoIdList.size() > 0) {
            vodClient.deleteVideoBatch(videoIdList);
        }

        // 根据courseId删除小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);

    }
}
