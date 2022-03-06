package com.atguigu.eduservice.service;

import com.atguigu.commonutils.ResultEntity;
import com.atguigu.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author peanut
 * @since 2022-02-28
 */
public interface EduCommentService extends IService<EduComment> {

    Map<String, Object> getPageComment(long currentPage, long limit, String courseId);

}
