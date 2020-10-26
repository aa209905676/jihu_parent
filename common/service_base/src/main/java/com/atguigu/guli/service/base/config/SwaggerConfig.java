package com.atguigu.guli.service.base.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 //开启在线接口文档
public class SwaggerConfig {
    //    配置Swagger2
    @Bean
    public Docket webApliConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webAplInfo())
                .select()      //只显示前端api
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();
    }

    @Bean  //后端swagger
    public Docket AdminwebApliConfig() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("adminApi")
                .apiInfo(webAplInfo())
                .select()      //只显示前端api
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }

    private ApiInfo webAplInfo() {
        return new ApiInfoBuilder()
                .title("Api文档")
                .description("几户学院的一些接口")
                .version("1.0")
                .contact(new Contact("jihu", "baidu.com", "209905676@qq.com"))
                .build();
    }
}
