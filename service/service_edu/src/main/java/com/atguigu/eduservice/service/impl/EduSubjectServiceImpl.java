package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.excel.ExcelSubjectData;
import com.atguigu.eduservice.entity.vo.subject.OneSubject;
import com.atguigu.eduservice.entity.vo.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author peanut
 * @since 2022-02-17
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void importSubjectData(MultipartFile file, EduSubjectService eduSubjectService) {
        try {
            // 获取文件的输入流
            InputStream inputStream = file.getInputStream();
            // 使用easyexcel读取excel文件
            EasyExcel.read(inputStream, ExcelSubjectData.class, new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20002, "添加课程分类失败");
        }
    }

    @Override
    public List<OneSubject> getOneSubjectList() {
        // 查找一级分类的课程
        QueryWrapper<EduSubject> oneSubjectQueryWrapper = new QueryWrapper<>();
        oneSubjectQueryWrapper.eq("parent_id", "0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(oneSubjectQueryWrapper);

        // 查找二级分类的课程
        QueryWrapper<EduSubject> twoSubjectQueryWrapper = new QueryWrapper<>();
        twoSubjectQueryWrapper.ne("parent_id", "0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(twoSubjectQueryWrapper);

        // new一个返回的一级课程的List集合
        ArrayList<OneSubject> finalOneSubjectList = new ArrayList<>();

        // 封装一级分类
        for (EduSubject oneSubject : oneSubjectList) {
            OneSubject tempOneSubject = new OneSubject();
            BeanUtils.copyProperties(oneSubject, tempOneSubject);

            // 在一级分类中封装二级分类
            for (EduSubject twoSubject : twoSubjectList) {
                // 判断二级分类的parent_id是否和一级分类的id相同，相同才封装进去
                if (twoSubject.getParentId().equals(oneSubject.getId())) {
                    TwoSubject tempTwoSubject = new TwoSubject();
                    BeanUtils.copyProperties(twoSubject, tempTwoSubject);
                    tempOneSubject.getChildren().add(tempTwoSubject);
                }
            }
            // 最后将临时的一级目录封装到返回的集合中
            finalOneSubjectList.add(tempOneSubject);
        }

        return finalOneSubjectList;
    }
}
