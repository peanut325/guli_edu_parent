package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.teacher.TeacherQuery;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author peanut
 * @since 2022-02-09
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    public HashMap<String, Object> getPageTeacherCondition(long currentPage, long limit, TeacherQuery teacherQuery) {
        // 创建Page对象，封装封装的内容
        Page<EduTeacher> eduTeacherPage = new Page<>(currentPage, limit);
        // 获取所有条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        // 判单条件是否存在来封装查询条件
        QueryWrapper<EduTeacher> eduTeacherQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            eduTeacherQueryWrapper.like("name", name);
        }

        if (!StringUtils.isEmpty(level)) {
            eduTeacherQueryWrapper.eq("level", level);
        }

        if (!StringUtils.isEmpty(begin)) {
            eduTeacherQueryWrapper.ge("gmt_create", begin);
        }

        if (!StringUtils.isEmpty(end)) {
            eduTeacherQueryWrapper.le("gmt_modified", end);
        }

        // 降序排序，方便新增后在页面第一条显示增的讲师
        eduTeacherQueryWrapper.orderByDesc("gmt_create");

        // 调用方法实现分页，会自动将分页数据封装到eduTeacherPage中
        this.page(eduTeacherPage, eduTeacherQueryWrapper);
        // 获取总页数
        long total = eduTeacherPage.getTotal();
        // 获取封装的记录
        List<EduTeacher> eduTeacherList = eduTeacherPage.getRecords();
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", eduTeacherList);
        return map;
    }

    @Override
    public HashMap<String, Object> getPageTeacher(long currentPage, long limit) {
        // 创建Page对象，封装封装的内容
        Page<EduTeacher> eduTeacherPage = new Page<>(currentPage, limit);
        // 调用方法实现分页，会自动将分页数据封装到eduTeacherPage中
        this.page(eduTeacherPage, null);
        // 获取总页数
        long total = eduTeacherPage.getTotal();
        // 获取封装的记录
        List<EduTeacher> eduTeacherList = eduTeacherPage.getRecords();
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", eduTeacherList);

        return map;
    }

    // 前台返回讲师的分页，与后台系统有区别
    @Override
    public Map<String, Object> getPageFrontTeacher(long currentPage, long limit) {
        Page<EduTeacher> eduTeacherPage = new Page<EduTeacher>(currentPage, limit);
        baseMapper.selectPage(eduTeacherPage, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageTeacherInfo", eduTeacherPage);
        map.put("hasNext", eduTeacherPage.hasNext());
        map.put("hasPrevious", eduTeacherPage.hasPrevious());
        return map;
    }
}
