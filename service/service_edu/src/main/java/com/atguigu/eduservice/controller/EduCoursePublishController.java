package com.atguigu.eduservice.controller;




import com.atguigu.commonutils.ResultEntity;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.publish.CoursePublishVO;
import com.atguigu.eduservice.service.EduCourseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/edu-publish")
//@CrossOrigin
public class EduCoursePublishController {

    @Autowired
    private EduCourseService eduCourseService;

    @ApiOperation(value = "根据courseId查询课程确认信息")
    @GetMapping("getCoursePublishVO/{courseId}")
    public ResultEntity getCoursePublishVO(@PathVariable String courseId) {
        CoursePublishVO coursePublishVO = eduCourseService.getCoursePublishVO(courseId);
        return ResultEntity.ok().data("coursePublish" , coursePublishVO);
    }

    @ApiOperation(value = "将课程信息的status修改为已发布")
    @PutMapping("updateCourseStatus/{courseId}")
    public ResultEntity updateCourseStatus(@PathVariable String courseId) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal");
        eduCourseService.updateById(eduCourse);
        return ResultEntity.ok();
    }
}
