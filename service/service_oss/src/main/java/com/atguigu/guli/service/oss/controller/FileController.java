package com.atguigu.guli.service.oss.controller;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.service.base.exception.GuLiExcpetion;
import com.atguigu.guli.service.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

@RestController
@Api(tags = "阿里云文件管理")
//@CrossOrigin // 跨域
@RequestMapping("/admin/oss/file")
@Slf4j
public class FileController {
    @Resource
    private FileService fileService;
    @ApiOperation("文件上传")
    @PostMapping("upload")
    public R upload(
            @ApiParam(value = "文件" ,required = true)
            @RequestParam("file")
            MultipartFile file,

            @ApiParam(value = "模块",readOnly = true)
            @RequestParam("module")
            String module
    ) {
        try {
            InputStream inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            String uploadUrl = fileService.upload(inputStream, module, filename);

            return R.ok().message("文件上传成功").data("url",uploadUrl);
        } catch (IOException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuLiExcpetion(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }

    }
    @ApiOperation(value = "删除图片")
    @DeleteMapping("remove")
    public R removeFile(
            @ApiParam("删除的文件URL路径")
            @RequestBody String url){
        fileService.removeFile(url);
        return R.ok().message("文件删除成功");
    }

    @ApiOperation(value = "测试")
    @GetMapping("test")
    public R test() {
        log.info("oss test被调用");
        return R.ok();
    }


}
