package com.atguigu.guli.infrastructure.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class InfrastructureApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfrastructureApiGatewayApplication.class, args);
    }
}