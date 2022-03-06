package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.front.FrontBaseCourseVO;
import com.atguigu.eduservice.entity.vo.publish.CoursePublishVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author peanut
 * @since 2022-02-18
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    CoursePublishVO getCoursePublishVO(@Param("courseId") String courseId);

    FrontBaseCourseVO getFrontCourseVO(@Param("courseId") String courseId);
}
