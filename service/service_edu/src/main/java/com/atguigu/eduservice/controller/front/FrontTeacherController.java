package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.ResultEntity;
import com.atguigu.commonutils.dto.TeacherInfoDO;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin
@RequestMapping("/eduservice/front-teacher")
public class FrontTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @Autowired
    private EduCourseService eduCourseService;

    @GetMapping("pageTeacher/{currentPage}/{limit}")
    public ResultEntity pageTeacher(@PathVariable long currentPage, @PathVariable long limit) {
        // 此时没有使用element-ui提供的分页组件，返回的参数与后台不一致
        Map<String, Object> map = eduTeacherService.getPageFrontTeacher(currentPage, limit);
        return ResultEntity.ok().data(map);
    }

    @GetMapping("getTeacherInfoById/{teacherId}")
    public ResultEntity getTeacherInfoById(@PathVariable String teacherId) {
        // 根据讲师id查询讲师的基本信息
        EduTeacher eduTeacher = eduTeacherService.getById(teacherId);

        // 根据讲师id查询课程的基本信息
        List<EduCourse> eduCourseList = eduCourseService.getCourseByTeacherId(teacherId);

        return ResultEntity.ok().data("teacher", eduTeacher).data("courseList", eduCourseList);
    }

    @GetMapping("getTeacherName/{teacherId}")
    public TeacherInfoDO getTeacherName(@PathVariable String teacherId) {
        // 根据讲师id查询讲师的基本信息
        EduTeacher eduTeacher = eduTeacherService.getById(teacherId);

        TeacherInfoDO teacherInfoDO = new TeacherInfoDO();
        BeanUtils.copyProperties(eduTeacher, teacherInfoDO);

        return teacherInfoDO;
    }
}
