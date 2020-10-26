package com.atguigu.guli.service.sms.Service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuLiExcpetion;
import com.atguigu.guli.service.sms.util.SmsProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {
    @Resource
    private SmsProperties smsProperties;

    @Override
    public void send(String mobile, String checkCode) throws ClientException {

        //调用短信发送SDK，创建client对象
        DefaultProfile profile = DefaultProfile.getProfile(
                smsProperties.getRegionId(),
                smsProperties.getKeyId(),
                smsProperties.getKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        //组装请求参数
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", smsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", smsProperties.getSignName());
        request.putQueryParameter("TemplateCode", smsProperties.getTemplateCode());

        Map<String, Object> param = new HashMap<>();
        param.put("code", checkCode);

        //将包含验证码的集合转换为json字符串
        Gson gson = new Gson();
        request.putQueryParameter("TemplateParam", gson.toJson(param));

        //发送短信
        CommonResponse response = client.getCommonResponse(request);

        //得到json字符串格式的响应结果
        String data = response.getData();

        //解析json字符串格式的响应结果
        HashMap<String, String> map = gson.fromJson(data, HashMap.class);
        String code = map.get("Code");
        String message = map.get("Message");

        //配置参考：短信服务->系统设置->国内消息设置
        //错误码参考：
        //https://help.aliyun.com/document_detail/101346.html?spm=a2c4g.11186623.6.613.3f6e2246sDg6Ry
        //控制所有短信流向限制（同一手机号：一分钟一条、一个小时五条、一天十条）
        if ("isv.BUSINESS_LIMIT_CONTROL".equals(code)) {
            log.error("短信发送过于频繁 " + "【code】" + code + ", 【message】" + message);
            throw new GuLiExcpetion(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
        }

        if (!"OK".equals(code)) {
            log.error("短信发送失败 " + " - code: " + code + ", message: " + message);
            throw new GuLiExcpetion(ResultCodeEnum.SMS_SEND_ERROR);
        }
    }
}