package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseQueryVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.service.CourseService;
import com.atguigu.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author wjh
 * @since 2020-08-13
 */
//@CrossOrigin //允许跨域
@Api(tags = "课程管理")
@RestController
@RequestMapping("/admin/edu/course")
@Slf4j
public class CourseControllerAdmin {

    @Resource
    private CourseService courseService;
    @Resource
    private VideoService videoService;
    @ApiOperation("新增课程")
    @PostMapping("save-course-info")
    public R saveCourseInfo(
            @ApiParam(value = "课程基本信息",required = true)
            @RequestBody CourseInfoForm courseInfoForm
    ){
      String courseId =  courseService.saveCourseInfo(courseInfoForm);
      return R.ok().data("courseId",courseId).message("保存成功");

    }

    @ApiOperation("根据ID查询课程")
    @GetMapping("course-info/{id}")
    public R getById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String id){
          CourseInfoForm CourseInfoForm =  courseService.getCourseInfoById(id);
          if (CourseInfoForm != null){
              return R.ok().data("item",CourseInfoForm);
          }else {
              return R.error().message("数据不存在");
          }

    }

    @ApiOperation("更新课程")
    @PutMapping("update-course-info")
    public R updateCourseInfoById(
            @ApiParam(value = "课程基本信息", required = true)
            @RequestBody CourseInfoForm courseInfoForm){

        courseService.updateCourseInfoById(courseInfoForm);
        return R.ok().message("修改成功");
    }

    @ApiOperation("分页课程列表")
    @GetMapping("list/{page}/{limit}")
    public R index(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(value = "查询对象")
                    CourseQueryVo courseQueryVo){
        IPage<CourseVo> pageModel =  courseService.selectPage(page,limit,courseQueryVo);
        List<CourseVo> records = pageModel.getRecords();
        long total = pageModel.getTotal();
        return R.ok().data("total",total).
                      data("rows",records);
    }

    @ApiOperation(value = "根据ID删除课程")
    @DeleteMapping("remove/{id}")
    public R removeByIdList(@ApiParam(value = "课程ID", required = true)
                            // 根据json取值
                            @PathVariable String  id){
//删除视频：VOD
        videoService.removeMediaVideoByCourseId(id);

        //删除封面：OSS
        courseService.removeCoverById(id);
        // 删除课程
        boolean result = courseService.removeCourseById(id);
        if(result){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据ID获取课程发布信息")
    @GetMapping("course-publish/{id}")
    public R getCoursePublishVoById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String id){
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVoById(id);

        if(coursePublishVo != null){
            return R.ok().data("item",coursePublishVo);
        }else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id发布课程")
    @PutMapping("publish-course/{id}")
    public R publishCourseById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String id){

        boolean result = courseService.publishCourseById(id);
        if (result) {
            return R.ok().message("发布成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

}

