package com.atguigu.guli.service.sms.Service;

import com.aliyuncs.exceptions.ClientException;

public interface SmsService {

    void send(String mobile, String checkCode) throws ClientException;
}