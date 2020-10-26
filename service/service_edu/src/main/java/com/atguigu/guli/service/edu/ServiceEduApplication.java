package com.atguigu.guli.service.edu;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.atguigu.guli"}) //扫描所有
@EnableDiscoveryClient //微服务注册 适用于所有微服务
@EnableFeignClients // 激活feign
public class ServiceEduApplication {
  
    public static void main(String[] args) {
        SpringApplication.run(ServiceEduApplication.class, args);
    }
}