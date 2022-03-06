package com.atguigu.orderservice.client;

import com.atguigu.commonutils.dto.CourseInfoDO;
import com.atguigu.commonutils.dto.TeacherInfoDO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-edu", fallback = com.atguigu.orderservice.client.EduDegradeFeignClient.class)
public interface EduClient {
    @GetMapping("/eduservice/front-course/getCourseInfoByCourseId/{courseId}")
    public CourseInfoDO getCourseInfoByCourseId(@PathVariable("courseId") String courseId);

    @GetMapping("/eduservice/front-teacher/getTeacherName/{teacherId}")
    public TeacherInfoDO getTeacherName(@PathVariable("teacherId") String teacherId);
}
