package com.atguigu.ucenterservice.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.atguigu.ucenterservice.utils.ConstantWechatUtil;
import com.atguigu.ucenterservice.utils.HttpClientUtils;
import com.google.gson.Gson;
import com.sun.deploy.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

//@CrossOrigin
@Controller    // 跳转页面
@RequestMapping("/ucenterservice/ucenter-wxlogin")
public class WechatLoginController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UcenterMemberService ucenterMemberService;

    // 扫码之后的回调
    @GetMapping("callback")
    public String callback(String code, String state) {
        // 获取redis中的state和传过来的state进行判断
        String redisState = redisTemplate.opsForValue().get("wechar-open-state");

        // 不相等抛出异常
        if (!state.equals(redisState)) {
            throw new GuliException(20001, "非法访问!");
        }

        // 使用httpclient，向认证服务器发送请求换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantWechatUtil.WX_OPEN_APP_ID,
                ConstantWechatUtil.WX_OPEN_APP_SECRET,
                code);

        String result = null;
        try {
            // 获取访问后返回的值
            result = HttpClientUtils.get(accessTokenUrl);
        } catch (Exception exception) {
            throw new GuliException(20001, "登录失败!");
        }

        // 使用gson解析返回的结果
        Gson gson = new Gson();
        HashMap map = gson.fromJson(result, HashMap.class);
        String accessToken = (String) map.get("access_token");
        String openId = (String) map.get("openid");

        // 查询数据库中是否存在登录过的用户
        UcenterMember ucenterMember = ucenterMemberService.getByOpenId(openId);
        String jwtToken = null;

        // 数据库中不存在，则可以使用微信登录
        if (ucenterMember == null) {
            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            // 拼接访问地址
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openId);

            String userInfoResult = null;
            // 获取返回的用户信息
            try {
                userInfoResult = HttpClientUtils.get(userInfoUrl);
            } catch (Exception exception) {
                throw new GuliException(20001, "登录失败!");
            }

            // 转化为字符串
            HashMap hashMap = gson.fromJson(userInfoResult, HashMap.class);
            // 获取用户名和头像
            String nickName = (String) hashMap.get("nickname");
            String headImgUrl = (String) hashMap.get("headimgurl");

            // 保存用户信息在数据库中
            UcenterMember member = new UcenterMember();
            member.setOpenid(openId);
            member.setAvatar(headImgUrl);
            member.setNickname(nickName);
            ucenterMemberService.save(member);

            // 直接存入session在不同的域名中会存在跨域，通过jwt生p成token，将首页显示用户的信息通过token传输，防止跨域问题
            jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        }
        // 跳转主页面
        return "redirect:http://localhost:3000?token=" + jwtToken;
    }

    @GetMapping("login")
    public String wechatLogin() {
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 回调地址
        String redirectUrl = ConstantWechatUtil.WX_OPEN_REDIRECT_URL;

        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (Exception exception) {
            throw new GuliException(20001, exception.getMessage());
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        // 创建标识，作为判断，一般情况可以使用随机数
        String state = "atguigu-peanut";
        // 并将标识存入redis中，设置过期事件为10分钟
        redisTemplate.opsForValue().set("wechar-open-state", state, 10, TimeUnit.MINUTES);

        // 拼接跳转路径
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantWechatUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state
        );
        return "redirect:" + qrcodeUrl;
    }

}
