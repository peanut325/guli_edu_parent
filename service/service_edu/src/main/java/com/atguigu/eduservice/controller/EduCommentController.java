package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.ResultEntity;
import com.atguigu.commonutils.dto.LoginInfoDO;
import com.atguigu.eduservice.client.CommentClient;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.service.EduCommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author peanut
 * @since 2022-02-28
 */
@RestController
@RequestMapping("/eduservice/edu-comment")
//@CrossOrigin
public class EduCommentController {

    @Autowired
    private EduCommentService eduCommentService;

    @Autowired
    private CommentClient commentClient;

    // 分页查询评论
    @GetMapping("getPageComment/{currentPage}/{limit}/{courseId}")
    public ResultEntity getPageEduComment(
            @PathVariable long currentPage,
            @PathVariable long limit,
            @PathVariable(required = false) String courseId) {
        Map<String, Object> map = eduCommentService.getPageComment(currentPage, limit, courseId);
        return ResultEntity.ok().data(map);
    }

    // 保存评论
    @PostMapping("saveComment")
    public ResultEntity saveComment(@RequestBody EduComment eduComment, HttpServletRequest request) {
        // 获取用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            return ResultEntity.error().message("请登录！");
        }

        // 调用ucenter的方法查询登录用户的信息
        LoginInfoDO loginInfoDO = commentClient.getLoginInfoById(memberId);

        BeanUtils.copyProperties(loginInfoDO, eduComment);
        eduComment.setMemberId(memberId);

        eduCommentService.save(eduComment);
        return ResultEntity.ok();
    }
}

