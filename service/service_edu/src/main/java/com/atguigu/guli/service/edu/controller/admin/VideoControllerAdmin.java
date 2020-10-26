package com.atguigu.guli.service.edu.controller.admin;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.Feign.VodMediaService;
import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
//@CrossOrigin
@Api(tags = "课时管理")
@RestController
@RequestMapping("/admin/edu/video")
@Slf4j
public class VideoControllerAdmin {
    @Resource
    private VideoService videoService;
 @Resource
 private VodMediaService vodMediaService;

    @ApiOperation("新增课时")
    @PostMapping("save")
    public R save(
            @ApiParam(value="课时对象", required = true)
            @RequestBody Video video){
        boolean result = videoService.save(video);
        if (result) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }


    @ApiOperation("根据id查询课时")
    @GetMapping("get/{id}")
    public R getById(
            @ApiParam(value="章节id", required = true)
            @PathVariable String id){

        Video video = videoService.getById(id);
        if (video != null) {
            return R.ok().data("item", video);
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id修改课时")
    @PutMapping("update")
    public R updateById(
            @ApiParam(value="章节对象", required = true)
            @RequestBody Video video){

        boolean result = videoService.updateById(video);
        if (result) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("数据不存在");
        }
    }



    @ApiOperation("根据ID删除课时")
    @DeleteMapping("remove/{id}")
    public R removeById(
            @ApiParam(value = "章节ID", required = true)
            @PathVariable String id){

       // 根据videoId根据阿里云视频
     videoService.removeVideo(id);

        boolean result = videoService.removeById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }
}