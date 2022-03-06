package com.atguigu.ucenterservice.service;

import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginInfoVO;
import com.atguigu.ucenterservice.entity.vo.LoginVO;
import com.atguigu.ucenterservice.entity.vo.RegisterVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author peanut
 * @since 2022-02-26
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(LoginVO loginVO);

    void register(RegisterVO registerVO);

    LoginInfoVO getLoginInfo(String memberIdByJwtToken);

    UcenterMember getByOpenId(String openId);

    int countRegisterDay(String day);
}
