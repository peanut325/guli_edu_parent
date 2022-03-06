package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.ResultEntity;
import com.atguigu.commonutils.dto.LoginInfoDO;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginInfoVO;
import com.atguigu.ucenterservice.entity.vo.LoginVO;
import com.atguigu.ucenterservice.entity.vo.RegisterVO;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author peanut
 * @since 2022-02-26
 */
@RestController
@RequestMapping("/ucenterservice/ucenter-member")
//@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @ApiOperation(value = "登录账号，返回token")
    @PostMapping("login")
    public ResultEntity userLogin(@RequestBody LoginVO loginVO) {
        String token = ucenterMemberService.login(loginVO);
        if (!StringUtils.isEmpty(token)) {
            return ResultEntity.ok().data("token", token);
        } else {
            return ResultEntity.error();
        }
    }

    @ApiOperation(value = "注册账号")
    @PostMapping("register")
    public ResultEntity userRegister(@RequestBody RegisterVO registerVO) {
        ucenterMemberService.register(registerVO);
        return ResultEntity.ok();
    }

    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("getLoginInfo")
    public ResultEntity getLoginInfo(HttpServletRequest request) {
        try {
            String memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);
            LoginInfoVO loginInfoVO = ucenterMemberService.getLoginInfo(memberIdByJwtToken);
            return ResultEntity.ok().data("loginInfoVO", loginInfoVO);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new GuliException(20001, "信息不存在!");
        }

    }

    // 用户id获取用户信息
    @GetMapping("getLoginInfoById/{memberId}")
    public LoginInfoDO getLoginInfoById(@PathVariable String memberId) {
        UcenterMember ucenterMember = ucenterMemberService.getById(memberId);
        LoginInfoDO loginInfoDO = new LoginInfoDO();
        BeanUtils.copyProperties(ucenterMember, loginInfoDO);
        return loginInfoDO;
    }

    // 查询当天的注册人数
    @GetMapping("countRegister/{day}")
    public ResultEntity countRegister(@PathVariable String day) {
        int countRegister = ucenterMemberService.countRegisterDay(day);
        return ResultEntity.ok().data("countRegister", countRegister);
    }
}

