package com.atguigu.guli.service.sms.controller;

import com.aliyuncs.exceptions.ClientException;
import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.util.FormUtils;
import com.atguigu.guli.common.base.util.RandomUtils;
import com.atguigu.guli.service.sms.Service.SmsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
@RefreshScope
//@CrossOrigin //跨域
@Slf4j
public class ApiSmsController {

   @Resource
   private RedisTemplate redisTemplate;
   @Resource
   private SmsService smsService;

    @GetMapping("send/{mobile}")
    public R getCode(@PathVariable String mobile) throws ClientException {
        // 验证手机号码是否合法
        if(StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)){
            log.info("请输入正确的手机号码");
            return R.error().message("请输入正确的手机号码");
        }
        // 生成验证码
        String checkCode = RandomUtils.getSixBitRandom();
//        发送验证码
//        smsService.send(mobile, checkCode);
//        将验证码存入redis缓存                                5分钟过期
        redisTemplate.opsForValue().set(mobile,checkCode,5, TimeUnit.MINUTES);

        return R.ok().message("短信发送成功");
    }
}
