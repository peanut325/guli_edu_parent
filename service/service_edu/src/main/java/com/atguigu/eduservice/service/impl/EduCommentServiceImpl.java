package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.ResultEntity;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.mapper.EduCommentMapper;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author peanut
 * @since 2022-02-28
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    @Override
    public Map<String, Object> getPageComment(long currentPage, long limit, String courseId) {
        Page<EduComment> eduCommentPage = new Page<>(currentPage, limit);

        QueryWrapper<EduComment> eduCommentQueryWrapper = new QueryWrapper<>();
        eduCommentQueryWrapper.eq("course_id", courseId);
        eduCommentQueryWrapper.orderByDesc("gmt_create");

        baseMapper.selectPage(eduCommentPage, eduCommentQueryWrapper);

        List<EduComment> eduCommentList = eduCommentPage.getRecords();

        Map<String, Object> map = new HashMap<>();
        map.put("items", eduCommentList);
        map.put("current", eduCommentPage.getCurrent());
        map.put("pages", eduCommentPage.getPages());
        map.put("size", eduCommentPage.getSize());
        map.put("total", eduCommentPage.getTotal());
        map.put("hasNext", eduCommentPage.hasNext());
        map.put("hasPrevious", eduCommentPage.hasPrevious());
        return map;
    }

}
