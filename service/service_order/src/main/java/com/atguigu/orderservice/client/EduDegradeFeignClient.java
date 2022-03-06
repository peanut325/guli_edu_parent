package com.atguigu.orderservice.client;

import com.atguigu.commonutils.dto.CourseInfoDO;
import com.atguigu.commonutils.dto.TeacherInfoDO;
import org.springframework.stereotype.Component;

@Component
public class EduDegradeFeignClient implements EduClient{

    @Override
    public CourseInfoDO getCourseInfoByCourseId(String courseId) {
        return null;
    }

    @Override
    public TeacherInfoDO getTeacherName(String teacherId) {
        return null;
    }
}
