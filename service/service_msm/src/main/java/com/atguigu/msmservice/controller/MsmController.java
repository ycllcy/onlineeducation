package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author LcY
 * @create 2022-09-12 21:31
 */
@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 发送短信的方法
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone) {
        // 首先从Redis中获取验证码，如果能获取到直接返回，如果获取不到，再使用阿里云进行发送
        String code = redisTemplate.opsForValue().get(phone);

        if (!StringUtils.isEmpty(code)) {
            return R.ok();
        }

        // 生成一个随机值，传递给阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        HashMap<String, Object> param = new HashMap<>();
        param.put("code", code);

        // 调用service中发送短信的方法
        boolean isSend = msmService.send(param, phone);

        if (isSend) {
            // 发送成功，把发送成功的验证码放到Redis中
            // 设置有效时间
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            return R.ok();
        } else {
            return R.error().message("短信发送失败！");
        }
    }
}
