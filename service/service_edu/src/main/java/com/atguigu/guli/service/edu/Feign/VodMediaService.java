package com.atguigu.guli.service.edu.Feign;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.Feign.fallback.VodMediaServiceFallBack;
import com.atguigu.guli.service.edu.Feign.fallback.osoFileServiceFallback;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@FeignClient(value = "service-vod",fallback = VodMediaServiceFallBack.class)
public interface VodMediaService {
    @DeleteMapping("/admin/vod/media/remove/{vodId}")
    R removeVideo(@PathVariable("vodId") String vodId);

    // 批量删除
    @DeleteMapping("remove")
    R removeVideoByIdList(@RequestBody List<String> videoIdList);
}