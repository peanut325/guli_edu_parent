package com.atguigu.eduservice.controller;



import com.atguigu.commonutils.ResultEntity;
import com.atguigu.eduservice.entity.vo.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author peanut
 * @since 2022-02-17
 */
@RestController
@RequestMapping("/eduservice/edu-subject")
//@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    // 添加课程分类
    @ApiOperation(value = "Excel批量导入")
    @PostMapping("addSubject")
    public ResultEntity addSubject(MultipartFile file) {
        // 调用上传方法
        eduSubjectService.importSubjectData(file, eduSubjectService);
        return ResultEntity.ok();
    }

    // 展示课程分类
    @ApiOperation(value = "展示课程分类")
    @GetMapping("listSubject")
    public ResultEntity listSubject() {
        List<OneSubject> oneSubjectList = eduSubjectService.getOneSubjectList();
        return ResultEntity.ok().data("list", oneSubjectList);
    }

}

