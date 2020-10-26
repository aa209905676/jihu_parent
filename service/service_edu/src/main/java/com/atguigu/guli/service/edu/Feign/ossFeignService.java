package com.atguigu.guli.service.edu.Feign;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.Feign.fallback.osoFileServiceFallback;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
// 通过在nacos下的微服务名称调用  fallback 如果远程调用失败，自动执行这个类里面的方法
@FeignClient(value = "service-oss",fallback = osoFileServiceFallback.class)

//@Component // 由于接口没有实现，无法注入，通过这个注解可以骗过springboot 2020版不用骗了
public interface ossFeignService {

    @GetMapping("/admin/oss/file/test")
    R test();

    @DeleteMapping("/admin/oss/file/remove")
    R removeFile(@RequestBody String url);

}
