package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.ResultEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
//@CrossOrigin // 解决跨域
public class EduLoginController {

    @PostMapping("login")
    public ResultEntity login() {
        return ResultEntity.ok().data("token", "admin");
    }

    @GetMapping("info")
    public ResultEntity info() {
        return ResultEntity.ok().data("roles", "[admin]").data("name", "admin").data("avatar", "https://project-guli-education.oss-cn-chengdu.aliyuncs.com/2022/02/17/1c592678eb374548a316df1e4eb92960file.png");
    }

}
