package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.ResultEntity;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
//@CrossOrigin
@RequestMapping("/eduservice/index")
public class FrontIndexController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduTeacherService eduTeacherService;

    @GetMapping("index")
    @Cacheable(key = "'index'" , value = "teacherAndCourseIndexInfo")
    public ResultEntity index() {
        // 根据课程id查询前8个
        QueryWrapper<EduCourse> eduCourseQueryWrapper = new QueryWrapper<>();
        eduCourseQueryWrapper.orderByDesc("id");
        eduCourseQueryWrapper.last("limit 8");
        List<EduCourse> eduCourseList = eduCourseService.list(eduCourseQueryWrapper);

        // 查询前4条名师
        QueryWrapper<EduTeacher> teacherQueryWrapper = new QueryWrapper<>();
        eduCourseQueryWrapper.orderByDesc("id");
        eduCourseQueryWrapper.last("limit 4");
        List<EduTeacher> eduTeacherList = eduTeacherService.list(teacherQueryWrapper);

        return ResultEntity.ok().data("eduList", eduCourseList).data("teacherList", eduTeacherList);

    }
}
