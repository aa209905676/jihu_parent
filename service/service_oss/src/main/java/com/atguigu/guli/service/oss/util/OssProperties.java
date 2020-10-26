package com.atguigu.guli.service.oss.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "aliyun.oss") //从配置文件读取值
public class OssProperties {
    private String endpoint;
    private String keyid;
    private String keysecret;
    private String bucketname;
}
