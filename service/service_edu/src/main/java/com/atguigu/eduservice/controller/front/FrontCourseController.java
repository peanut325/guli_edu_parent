package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.ResultEntity;
import com.atguigu.commonutils.dto.CourseInfoDO;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.vo.chapter.ChapterVO;
import com.atguigu.eduservice.entity.vo.course.CourseInfoVO;
import com.atguigu.eduservice.entity.vo.front.FrontBaseCourseVO;
import com.atguigu.eduservice.entity.vo.front.FrontCourseQueryVO;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin
@RequestMapping("/eduservice/front-course")
public class FrontCourseController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private OrderClient orderClient;

    @PostMapping("getPageCourseByCondition/{currentPage}/{limit}")
    public ResultEntity getPageCourseByCondition(
            @PathVariable long currentPage,
            @PathVariable long limit,
            @RequestBody(required = false) FrontCourseQueryVO courseQueryVO
    ) {
        Map<String, Object> map = eduCourseService.getPageCourseByCondition(currentPage, limit, courseQueryVO);
        return ResultEntity.ok().data(map);
    }

    @GetMapping("getFrontBaseCourse/{courseId}")
    public ResultEntity getFrontBaseCourse(@PathVariable String courseId, HttpServletRequest request) {
        // 查询课程的VO对象（sql语句查询）
        FrontBaseCourseVO frontBaseCourseVO = eduCourseService.getFrontBaseCourse(courseId);

        // 根据课程id查询章节
        List<ChapterVO> chapterVOList = eduChapterService.getChapterVO(courseId);

        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        boolean isBuyCount = false;

        if (StringUtils.isEmpty(memberId)) {
            return ResultEntity.ok().code(28004);
        }
            isBuyCount = orderClient.isBuy(courseId, memberId);

        return ResultEntity.ok().data("chapterList", chapterVOList).data("courseVO", frontBaseCourseVO).data("isBuy", isBuyCount);
    }

    @GetMapping("getCourseInfoByCourseId/{courseId}")
    public CourseInfoDO getCourseInfoByCourseId(@PathVariable String courseId) {
        CourseInfoVO courseInfoVO = eduCourseService.getCourseInfoVO(courseId);
        CourseInfoDO courseInfoDO = new CourseInfoDO();
        BeanUtils.copyProperties(courseInfoVO, courseInfoDO);
        return courseInfoDO;
    }
}
