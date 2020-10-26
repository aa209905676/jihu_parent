package com.atguigu.guli.service.base.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement // 开启事物
@Configuration
@MapperScan("com.atguigu.guli.service.*.mapper")
public class MybatisPlusConfig {

    /**
     9
     * 分页插件
     10
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {

        return new PaginationInterceptor();
    }
}
