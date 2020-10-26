package com.atguigu.guli.service.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.atguigu.guli"})
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceTradeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceTradeApplication.class, args);
	}
}