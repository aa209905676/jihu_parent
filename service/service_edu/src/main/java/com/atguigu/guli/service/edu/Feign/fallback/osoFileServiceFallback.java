package com.atguigu.guli.service.edu.Feign.fallback;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.Feign.ossFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

// 熔断之后的方法
@Slf4j
@Service
public class osoFileServiceFallback  implements ossFeignService {
    @Override
    public R test() {
        return R.error();
    }

    @Override
    public R removeFile(String url) {
        log.info("熔断保护");
        return R.error();
    }
}
