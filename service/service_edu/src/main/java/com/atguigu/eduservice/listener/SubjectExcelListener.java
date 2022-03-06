package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.excel.ExcelSubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Map;

public class SubjectExcelListener extends AnalysisEventListener<ExcelSubjectData> {

    // 因为SubjectExcelListener不能被spring管理（官方文档），所以要创建eduSubjectService不能使用@Autowired直接注入
    // 可以使用参数构造进去,但在controller中需要传入eduSubjectService
    public EduSubjectService eduSubjectService;

    //创建有参数构造，传递subjectService用于操作数据库
    public SubjectExcelListener() {
    }

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    // 在读取excel文件，一行一行的读取
    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        if (excelSubjectData == null) {
            // 文件中无内容，则抛出异常
            throw new GuliException(20001, "文件数据为空!");
        }

        // 先判断一级分类是否存在
        EduSubject eduSubjectOne = this.existOneSubject(eduSubjectService, excelSubjectData.getOneSubjectName());
        // 不存在，即保存一级分类
        if (eduSubjectOne == null) {
            // 相当于清空数据
            eduSubjectOne = new EduSubject();
            eduSubjectOne.setTitle(excelSubjectData.getOneSubjectName());
            eduSubjectOne.setParentId("0");
            eduSubjectService.save(eduSubjectOne);
        }

        // 先获取一级分类的id
        String pid = eduSubjectOne.getId();
        // 先判断二级分类是否存在
        EduSubject eduSubjectTwo = this.existTwoSubject(eduSubjectService, excelSubjectData.getTwoSubjectName(), pid);
        // 不存在，即保存二级分类
        if (eduSubjectTwo == null) {
            // 相当于清空数据
            eduSubjectTwo = new EduSubject();
            eduSubjectTwo.setTitle(excelSubjectData.getTwoSubjectName());
            eduSubjectTwo.setParentId(pid);
            eduSubjectService.save(eduSubjectTwo);
        }


    }

    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {

    }

    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    // 判断一级分类是否重复添加
    private EduSubject existOneSubject(EduSubjectService eduSubjectService, String name) {
        // 先查找一级分类是否存在
        QueryWrapper<EduSubject> eduSubjectQueryWrapper = new QueryWrapper<>();
        eduSubjectQueryWrapper.eq("title", name);
        eduSubjectQueryWrapper.eq("parent_id", "0");
        EduSubject eduSubjectOne = eduSubjectService.getOne(eduSubjectQueryWrapper);
        return eduSubjectOne;
    }

    // 判断二级分类是否重复添加
    private EduSubject existTwoSubject(EduSubjectService eduSubjectService, String name, String pid) {
        // 先查找一级分类是否存在
        QueryWrapper<EduSubject> eduSubjectQueryWrapper = new QueryWrapper<>();
        eduSubjectQueryWrapper.eq("title", name);
        eduSubjectQueryWrapper.eq("parent_id", pid);
        EduSubject eduSubjectTwo = eduSubjectService.getOne(eduSubjectQueryWrapper);
        return eduSubjectTwo;
    }

}
