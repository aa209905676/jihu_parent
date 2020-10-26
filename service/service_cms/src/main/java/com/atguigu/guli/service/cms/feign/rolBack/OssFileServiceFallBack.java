package com.atguigu.guli.service.cms.feign.rolBack;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.cms.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService {

    @Override
    public R removeFile(String url) {
        log.info("熔断保护");
        return R.error().message("调用超时");
    }
}
