package com.atguigu.eduservice.controller;




import com.atguigu.commonutils.ResultEntity;
import com.atguigu.eduservice.entity.vo.course.CourseInfoVO;
import com.atguigu.eduservice.entity.vo.course.CourseQuery;
import com.atguigu.eduservice.service.EduCourseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author peanut
 * @since 2022-02-18
 */
@RestController
@RequestMapping("/eduservice/edu-course")
//@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;

    @ApiOperation(value = "新增课程")
    @PostMapping("saveCourseInfo")
    public ResultEntity saveCourseInfo(
            @ApiParam(name = "CourseInfoForm", value = "课程基本信息", required = true)
            @RequestBody CourseInfoVO courseInfoVO
    ) {
        String eduCourseId = eduCourseService.saveCourseInfo(courseInfoVO);
        if (!StringUtils.isEmpty(eduCourseId)) {
            return ResultEntity.ok().data("eduCourseId", eduCourseId);
        } else {
            return ResultEntity.error();
        }
    }

    @ApiOperation(value = "查询课程用于回显")
    @GetMapping("getCourseInfo/{courseId}")
    public ResultEntity getCourseInfo(
            @ApiParam(name = "CourseId", value = "课程id", required = true)
            @PathVariable String courseId
    ) {
        CourseInfoVO courseInfoVO = eduCourseService.getCourseInfoVO(courseId);
        return ResultEntity.ok().data("courseInfo", courseInfoVO);
    }

    @ApiOperation(value = "修改课程")
    @PutMapping("updateCourseInfo")
    public ResultEntity updateCourseInfo(
            @ApiParam(name = "CourseInfoForm", value = "课程基本信息", required = true)
            @RequestBody CourseInfoVO courseInfoVO
    ) {
        try {
            eduCourseService.updateEduCourse(courseInfoVO);
            return ResultEntity.ok();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResultEntity.error();
        }
    }

    @ApiOperation(value = "分页带条件查询课程")
    @PostMapping("getPageCourseCondition/{currentPage}/{limit}")
    public ResultEntity getPageCourseCondition(
            @PathVariable long currentPage,
            @PathVariable long limit,
            @RequestBody(required = false) CourseQuery courseQuery) {
        Map<String, Object> eduCourseMap = eduCourseService.getPageCourseCondition(currentPage, limit, courseQuery);
        return ResultEntity.ok().data("eduCourseList", eduCourseMap);
    }

    @ApiOperation(value = "根据courseId删除课程")
    @DeleteMapping("deleteCourseById/{courseId}")
    public ResultEntity deleteCourseById(@PathVariable String courseId) {
        eduCourseService.deleteCourseAllPart(courseId);
        return ResultEntity.ok();
    }

}

