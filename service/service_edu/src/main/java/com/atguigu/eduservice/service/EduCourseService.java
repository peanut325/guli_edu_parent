package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.course.CourseInfoVO;
import com.atguigu.eduservice.entity.vo.course.CourseQuery;
import com.atguigu.eduservice.entity.vo.front.FrontBaseCourseVO;
import com.atguigu.eduservice.entity.vo.front.FrontCourseQueryVO;
import com.atguigu.eduservice.entity.vo.publish.CoursePublishVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author peanut
 * @since 2022-02-18
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVO courseInfoVO);

    CourseInfoVO getCourseInfoVO(String courseId);

    void updateEduCourse(CourseInfoVO courseInfoVO);

    CoursePublishVO getCoursePublishVO(String courseId);

    Map<String, Object> getPageCourseCondition(long currentPage, long limit, CourseQuery courseQuery);

    void deleteCourseAllPart(String courseId);

    List<EduCourse> getCourseByTeacherId(String teacherId);

    Map<String, Object> getPageCourseByCondition(long currentPage, long limit, FrontCourseQueryVO courseQueryVO);

    FrontBaseCourseVO getFrontBaseCourse(String courseId);
}
