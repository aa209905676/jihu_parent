package com.atguigu.guli.service.edu.Feign.fallback;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.Feign.VodMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class VodMediaServiceFallBack  implements VodMediaService {
    @Override
    public R removeVideo(String vodId) {
        log.info("熔断保护");
        return R.error();
    }

    @Override
    public R removeVideoByIdList(List<String> videoIdList) {
        log.info("熔断保护");
        return R.error();
    }
}
