package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.vo.course.CourseInfoVO;
import com.atguigu.eduservice.entity.vo.course.CourseQuery;
import com.atguigu.eduservice.entity.vo.front.FrontBaseCourseVO;
import com.atguigu.eduservice.entity.vo.front.FrontCourseQueryVO;
import com.atguigu.eduservice.entity.vo.publish.CoursePublishVO;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author peanut
 * @since 2022-02-18
 */
@Service
@Transactional(readOnly = true)
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduChapterService eduChapterService;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String saveCourseInfo(CourseInfoVO courseInfoVO) {
        if (courseInfoVO != null) {
            // 将VO对象的部分EduCourse数据保存在EduCourse中
            EduCourse eduCourse = new EduCourse();
            BeanUtils.copyProperties(courseInfoVO, eduCourse);
            boolean saveEduCourseFlag = this.save(eduCourse);
            if (!saveEduCourseFlag) {
                throw new GuliException(20001, "课程保存失败！");
            }

            // 将VO对象的部分EduCourseDescription数据保存在EduCourseDescription中
            EduCourseDescription eduCourseDescription = new EduCourseDescription();
            BeanUtils.copyProperties(courseInfoVO, eduCourseDescription);
            // 获取eduCourse中的的id设置到eduCourseDescription的id中,需要将主键策略修改为type = IdType.INPUT
            String eduCourseId = eduCourse.getId();
            eduCourseDescription.setId(eduCourseId);
            boolean saveEduCourseDescriptionFlag = eduCourseDescriptionService.save(eduCourseDescription);
            if (!saveEduCourseDescriptionFlag) {
                throw new GuliException(20001, "课程详情信息保存失败");
            }

            return eduCourseId;
        }
        return null;
    }

    @Override
    public CourseInfoVO getCourseInfoVO(String courseId) {
        // 创建返回的VO对象
        CourseInfoVO courseInfoVO = new CourseInfoVO();

        // 通过courseId查询Course
        EduCourse eduCourse = baseMapper.selectById(courseId);
        BeanUtils.copyProperties(eduCourse, courseInfoVO);

        // 根据eduCourse的id查询简述
        String id = eduCourse.getId();
        EduCourseDescription courseDescription = eduCourseDescriptionService.getById(id);
        BeanUtils.copyProperties(courseDescription, courseInfoVO);

        return courseInfoVO;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateEduCourse(CourseInfoVO courseInfoVO) {
        if (courseInfoVO != null) {
            // 保存课程信息
            EduCourse eduCourse = new EduCourse();
            BeanUtils.copyProperties(courseInfoVO, eduCourse);
            int flag = baseMapper.updateById(eduCourse);
            if (flag == 0) {
                throw new GuliException(20001, "修改课程失败!");
            }

            // 保存简述信息
            EduCourseDescription eduCourseDescription = new EduCourseDescription();
            BeanUtils.copyProperties(courseInfoVO, eduCourseDescription);
            boolean b = eduCourseDescriptionService.updateById(eduCourseDescription);
            if (!b) {
                throw new GuliException(20001, "修改课程失败!");
            }

        } else {
            throw new GuliException(20001, "修改课程失败，数据为空!");
        }
    }

    @Override
    public CoursePublishVO getCoursePublishVO(String courseId) {
        return baseMapper.getCoursePublishVO(courseId);
    }

    @Override
    public Map<String, Object> getPageCourseCondition(long currentPage, long limit, CourseQuery courseQuery) {
        // 创建分页对象
        Page<EduCourse> eduCoursePage = new Page<>(currentPage, limit);

        // 获取查询的条件
        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();

        // 判断courseQuery中的条件是否存在来封装wapper
        QueryWrapper<EduCourse> eduCourseQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(title)) {
            eduCourseQueryWrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            eduCourseQueryWrapper.eq("teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            eduCourseQueryWrapper.eq("subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            eduCourseQueryWrapper.eq("subject_id", subjectId);
        }

        // 查询已发布的内容
        eduCourseQueryWrapper.eq("status", "Normal");

        // 降序排序，方便新增后在页面第一条显示新增的课程
        eduCourseQueryWrapper.orderByDesc("gmt_create");

        // 查询返回集合
        baseMapper.selectPage(eduCoursePage, eduCourseQueryWrapper);

        // 获取总记录数
        long total = eduCoursePage.getTotal();
        // 获取查询后的集合
        List<EduCourse> eduCourseList = eduCoursePage.getRecords();
        // 封装进map集合
        HashMap<String, Object> eduCourseMap = new HashMap<>();
        eduCourseMap.put("total", total);
        eduCourseMap.put("rows", eduCourseList);

        return eduCourseMap;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteCourseAllPart(String courseId) {
        // 删除小节内容
        eduVideoService.removeEduVideoByCourseId(courseId);
        // 删除课程描述
        eduCourseDescriptionService.removeById(courseId);
        // 删除章节
        eduChapterService.removeById(courseId);

        // 根据id删除课程本身
        int flag = baseMapper.deleteById(courseId);
        if (flag == 0) {
            throw new GuliException(20001, "删除失败！");
        }
    }

    @Override
    public List<EduCourse> getCourseByTeacherId(String teacherId) {
        QueryWrapper<EduCourse> eduCourseQueryWrapper = new QueryWrapper<>();
        eduCourseQueryWrapper.eq("teacher_id", teacherId);
        List<EduCourse> eduCourseList = baseMapper.selectList(eduCourseQueryWrapper);
        return eduCourseList;
    }

    // 前台条件查询
    @Override
    public Map<String, Object> getPageCourseByCondition(long currentPage, long limit, FrontCourseQueryVO courseQueryVO) {
        Page<EduCourse> eduCoursePage = new Page<>(currentPage , limit);
        QueryWrapper<EduCourse> eduCourseQueryWrapper = new QueryWrapper<>();

        // 获取条件
        String subjectParentId = courseQueryVO.getSubjectParentId();
        String subjectId = courseQueryVO.getSubjectId();
        String buyCountSort = courseQueryVO.getBuyCountSort();
        String gmtCreateSort = courseQueryVO.getGmtCreateSort();
        String priceSort = courseQueryVO.getPriceSort();

        // 判断条件中的值是否存在，在进行封装条件
        if (!StringUtils.isEmpty(subjectParentId)) { // 一级分类
            eduCourseQueryWrapper.eq("subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {       // 二级分类
            eduCourseQueryWrapper.eq("subject_id", subjectId);
        }
        if (!StringUtils.isEmpty(buyCountSort)) {
            eduCourseQueryWrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(gmtCreateSort)) {
            eduCourseQueryWrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(priceSort)) {
            eduCourseQueryWrapper.orderByDesc("price");
        }
        eduCourseQueryWrapper.eq("status","Normal");

        // 查询对象
        baseMapper.selectPage(eduCoursePage, eduCourseQueryWrapper);

        List<EduCourse> records = eduCoursePage.getRecords();
        long current = eduCoursePage.getCurrent();
        long pages = eduCoursePage.getPages();
        long size = eduCoursePage.getSize();
        long total = eduCoursePage.getTotal();
        boolean hasNext = eduCoursePage.hasNext();
        boolean hasPrevious = eduCoursePage.hasPrevious();

        // 封装在map中返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public FrontBaseCourseVO getFrontBaseCourse(String courseId) {
        return baseMapper.getFrontCourseVO(courseId);
    }
}
