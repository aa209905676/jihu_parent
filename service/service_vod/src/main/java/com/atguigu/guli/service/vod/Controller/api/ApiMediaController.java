package com.atguigu.guli.service.vod.Controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuLiExcpetion;
import com.atguigu.guli.service.vod.Service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags="阿里云视频点播")
//@CrossOrigin //跨域
@RestController
@RequestMapping("/api/vod/media")
@Slf4j
public class ApiMediaController {
    @Resource
    private VideoService videoService;

    @GetMapping("get-play-auth/{videoSourceId}")
    public R getPlayAuth(
            @ApiParam(value = "阿里云视频文件的id", required = true)
            @PathVariable String videoSourceId){

        try{
            String playAuth = videoService.getPlayAuth(videoSourceId);
            return  R.ok().message("获取播放凭证成功").data("playAuth", playAuth);
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuLiExcpetion( ResultCodeEnum.FETCH_PLAYAUTH_ERROR);
        }
    }
}   