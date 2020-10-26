package com.atguigu.guli.service.vod.Controller.admin;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.service.base.exception.GuLiExcpetion;
import com.atguigu.guli.service.vod.Service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Api(tags = "阿里云视频点播")
@RestController
@Slf4j
@RequestMapping("/admin/vod/media")
//@CrossOrigin
public class MediaController {
    @Resource
    private VideoService videoService;

    @PostMapping("upload")
    public R uploadVideo(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String videoId = videoService.uploadVideo(inputStream, originalFilename);
            return R.ok().message("视频上传成功").data("videoId", videoId);
        } catch (IOException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuLiExcpetion(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }

    @DeleteMapping("remove/{vodId}")
    public R removeVideo(
            @ApiParam(value="阿里云视频id", required = true)
            @PathVariable String vodId){
        try {
            videoService.removeVideo(vodId);
            return R.ok().message("删除成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuLiExcpetion(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }
   // 批量删除
    @DeleteMapping("remove")
    public R removeVideoByIdList(
            @ApiParam(value = "阿里云视频id列表", required = true)
            @RequestBody List<String> videoIdList){

        try {
            videoService.removeVideoByIdList(videoIdList);
            return  R.ok().message("视频删除成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuLiExcpetion(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }
}