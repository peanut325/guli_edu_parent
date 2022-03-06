package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author peanut
 * @since 2022-02-18
 */
public interface EduVideoService extends IService<EduVideo> {

    boolean removeEduVideo(String id);

    void removeEduVideoByCourseId(String courseId);
}
