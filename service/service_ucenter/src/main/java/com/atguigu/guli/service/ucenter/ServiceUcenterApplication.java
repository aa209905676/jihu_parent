package com.atguigu.guli.service.ucenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.atguigu.guli"})
@EnableDiscoveryClient
public class ServiceUcenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceUcenterApplication.class, args);
	}
}