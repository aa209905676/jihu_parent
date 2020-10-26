package com.atguigu.guli.service.cms.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

//@CrossOrigin //解决跨域问题
@Api(tags = "广告推荐")
@RestController
@RequestMapping("/api/cms/ad")
public class ApiAdController {

    @Resource
    private AdService adService;
    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation("根据推荐位id显示广告推荐")
    @GetMapping("list/{adTypeId}")
    public R listByAdTypeId(@ApiParam(value = "推荐位id", required = true) @PathVariable String adTypeId) {
        List<Ad> ads = adService.selectByAdTypeId(adTypeId);
        return R.ok().data("items", ads);
    }

}