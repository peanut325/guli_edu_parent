package com.atguigu.ucenterservice.service.impl;

import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginInfoVO;
import com.atguigu.ucenterservice.entity.vo.LoginVO;
import com.atguigu.ucenterservice.entity.vo.RegisterVO;
import com.atguigu.ucenterservice.mapper.UcenterMemberMapper;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author peanut
 * @since 2022-02-26
 */
@Service
@Transactional(readOnly = true)
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String login(LoginVO loginVO) {
        // 判断获取的loginVO的手机号和密码是否为空
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();

        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "登录失败!");
        }

        // 从数据库中查询phone对应的对象
        QueryWrapper<UcenterMember> ucenterMemberQueryWrapper = new QueryWrapper<>();
        ucenterMemberQueryWrapper.eq("mobile", mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(ucenterMemberQueryWrapper);

        // 无对象，则抛出异常
        if (ucenterMember == null) {
            throw new GuliException(20001, "登陆失败!");
        }

        // 校验密码
        if (!MD5.encrypt(password).equals(ucenterMember.getPassword())) {
            throw new GuliException(20001, "登陆失败!");
        }

        // 校验用户是否被禁用
        if (ucenterMember.getIsDisabled()) {
            throw new GuliException(20001, "登陆失败!");
        }

        String jwtToken = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());
        return jwtToken;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void register(RegisterVO registerVO) {
        // 获取注册信息
        //获取注册信息，进行校验
        String nickname = registerVO.getNickname();
        String mobile = registerVO.getMobile();
        String password = registerVO.getPassword();
        String code = registerVO.getCode();

        // 判断VO的属性值是否为空
        if (StringUtils.isEmpty(mobile)
                || StringUtils.isEmpty(nickname)
                || StringUtils.isEmpty(code)
                || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "注册失败!");
        }

        // 从redis中取出验证码，注意autowired时写明泛型
        String redisCode = redisTemplate.opsForValue().get(mobile);
        // 校验验证码是否为空或相等
        if (StringUtils.isEmpty(redisCode) || !code.equals(redisCode)) {
            throw new GuliException(20001, "注册失败!");
        }

        // 查询数据库中是否有相同手机号的对象
        QueryWrapper<UcenterMember> ucenterMemberQueryWrapper = new QueryWrapper<>();
        ucenterMemberQueryWrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(ucenterMemberQueryWrapper);
        if (count == 0) {
            throw new GuliException(20001, "注册失败!");
        }

        // 将数据保存到数据库
        UcenterMember ucenterMember = new UcenterMember();
        BeanUtils.copyProperties(registerVO, ucenterMember);
        // 将密码加密后存储
        ucenterMember.setPassword(MD5.encrypt(password));
        // 禁用默认为false
        ucenterMember.setIsDisabled(false);
        // 设置默认头像
        ucenterMember.setAvatar("https://project-guli-education.oss-cn-chengdu.aliyuncs.com/2022/02/17/1c592678eb374548a316df1e4eb92960file.png");
        baseMapper.insert(ucenterMember);
        // 插入数据完成后将redis中的验证码清除
        redisTemplate.delete(mobile);
    }

    @Override
    public LoginInfoVO getLoginInfo(String memberIdByJwtToken) {
        if (StringUtils.isEmpty(memberIdByJwtToken)) {
            throw new GuliException(20001, "信息为空!");
        }

        // 根据id查询数据库
        LoginInfoVO loginInfoVO = new LoginInfoVO();
        UcenterMember ucenterMember = baseMapper.selectById(memberIdByJwtToken);
        BeanUtils.copyProperties(ucenterMember , loginInfoVO);

        return loginInfoVO;
    }

    @Override
    public UcenterMember getByOpenId(String openId) {
        QueryWrapper<UcenterMember> ucenterMemberQueryWrapper = new QueryWrapper<>();
        ucenterMemberQueryWrapper.eq("openid" , openId);
        UcenterMember ucenterMember = baseMapper.selectOne(ucenterMemberQueryWrapper);
        return ucenterMember;
    }

    @Override
    public int countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }
}
