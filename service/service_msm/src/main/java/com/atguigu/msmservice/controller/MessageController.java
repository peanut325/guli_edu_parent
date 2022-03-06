package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.ResultEntity;
import com.atguigu.msmservice.service.MsgService;
import com.atguigu.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

//@CrossOrigin
@RestController
@RequestMapping("/msmservice/msm-msgservice")
public class MessageController {

    @Autowired
    private MsgService msgService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("send/{phone}")
    public ResultEntity sendCode(@PathVariable String phone) {
        // 先在redis中获取验证码
        String code = redisTemplate.opsForValue().get(phone);

        // 判断验证码是否存在
        if (!StringUtils.isEmpty(code)) {
            return ResultEntity.ok();
        }

        // 工具类获取6位的验证码
        code = RandomUtil.getSixBitRandom();
        // 调用service发送验证码
        boolean ifSend = msgService.sendMessage(phone, code);
        if (ifSend) {
            // 将验证码存入redis，并设置5分钟过期
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return ResultEntity.ok();
        }else {
            return ResultEntity.error().message("发送短信失败");
        }
    }

}
