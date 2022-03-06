package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.teacher.TeacherQuery;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author peanut
 * @since 2022-02-09
 */
public interface EduTeacherService extends IService<EduTeacher> {

    HashMap<String, Object> getPageTeacherCondition(long currentPage, long limit, TeacherQuery teacherQuery);

    HashMap<String, Object> getPageTeacher(long currentPage, long limit);

    Map<String, Object> getPageFrontTeacher(long currentPage, long limit);
}
