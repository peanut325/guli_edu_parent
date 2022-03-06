package com.atguigu.eduservice.controller;



import com.atguigu.commonutils.ResultEntity;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.teacher.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author peanut
 * @since 2022-02-09
 */
@Api(tags = "讲师管理")
@RestController
@RequestMapping("/eduservice/edu-teacher")
//@CrossOrigin
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @ApiOperation(value = "查询所有讲师列表")
    @GetMapping("findAll")
    public ResultEntity findAllTeacher() {
        List<EduTeacher> eduTeacherList = eduTeacherService.list(null);
        return ResultEntity.ok().data("items", eduTeacherList);
    }

    @ApiOperation(value = "根据id逻辑删除讲师")
    @DeleteMapping("{id}")
    public ResultEntity removeTeacher(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable("id") String id) {
        boolean flag = eduTeacherService.removeById(id);
        if (flag) {
            return ResultEntity.ok();
        } else {
            return ResultEntity.error();
        }
    }

    @ApiOperation(value = "分页查询讲师")
    @GetMapping("pageTeacher/{currentPage}/{limit}")
    public ResultEntity pageTeacher(
            @ApiParam(name = "currentPage", value = "当前页码", required = true)
            @PathVariable("currentPage") long currentPage,
            @ApiParam(name = "limit", value = "本页数量", required = true)
            @PathVariable("limit") long limit
    ) {
        HashMap<String, Object> map = eduTeacherService.getPageTeacher(currentPage , limit);
        return ResultEntity.ok().data(map);
    }

    @ApiOperation(value = "分页带条件查询讲师")
    @PostMapping("pageTeacherCondition/{currentPage}/{limit}")
    public ResultEntity pageTeacherCondition(
            @ApiParam(name = "currentPage", value = "当前页码", required = true)
            @PathVariable("currentPage") long currentPage,
            @ApiParam(name = "limit", value = "本页数量", required = true)
            @PathVariable("limit") long limit,
            @ApiParam(name = "teacherQuery", value = "查询条件", required = true)
            // 使用@RequestBody时，前端传入的时json数据，且需要使用post请求
            @RequestBody(required = false) TeacherQuery teacherQuery
    ) {
        HashMap<String, Object> map = eduTeacherService.getPageTeacherCondition(currentPage , limit ,teacherQuery);
        return ResultEntity.ok().data(map);
    }

    @ApiOperation(value = "新增讲师")
    @PostMapping("addTeacher")
    public ResultEntity addTeacher(
            @ApiParam(name = "eduTeacher", value = "讲师信息", required = true)
            @RequestBody EduTeacher eduTeacher
    ) {
        boolean flag = eduTeacherService.save(eduTeacher);
        if (flag) {
            return ResultEntity.ok();
        } else {
            return ResultEntity.error();
        }
    }

    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("{id}")
    public ResultEntity getTeacherById(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable("id") String id
    ) {
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return ResultEntity.ok().data("teacher", eduTeacher);
    }

    @ApiOperation(value = "根据id修改讲师")
    @PutMapping("{id}")
    public ResultEntity updateById(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable("id") String id,
            @ApiParam(name = "eduTeacher", value = "讲师信息", required = true)
            @RequestBody EduTeacher eduTeacher
    ) {
        // 将需要修改的id值设置到eduTeacher中
        eduTeacher.setId(id);
        // 修改讲师数据
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if (flag) {
            return ResultEntity.ok();
        } else {
            return ResultEntity.error();
        }
    }

}

