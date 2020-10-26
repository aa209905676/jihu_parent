package com.atguigu.guli.service.edu.controller.API;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.util.JwtInfo;
import com.atguigu.guli.common.base.util.JwtUtils;
import com.atguigu.guli.service.edu.entity.vo.CourseCollectVo;
import com.atguigu.guli.service.edu.service.CourseCollectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.bootstrap.HttpServer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

//@CrossOrigin
@RestController
@RequestMapping("/api/edu/course-collect")
@Slf4j
public class ApiCourseCollectController {
    @Resource
    private CourseCollectService courseCollectService;

    @ApiOperation(value = "判断是否收藏")
    @GetMapping("auth/is-collect/{courseId}")
    public R isCollect(
            @ApiParam(value = "课程ID",required = true)
              @PathVariable String courseId,
               HttpServletRequest  request
    ){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
      boolean  isCollect =    courseCollectService.isCollect(courseId,jwtInfo.getId());
      return R.ok().data("isCollect",isCollect);
    }

    @ApiOperation(value = "收藏课程")
    @PostMapping("auth/save/{courseId}")
    public R save(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId,
            HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        courseCollectService.saveCourseCollect(courseId,jwtInfo.getId());
        return R.ok();
    }
    @ApiOperation(value = "获取课程收藏列表")
    @GetMapping("auth/list")
    public R collectList(HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        List<CourseCollectVo>  list =  courseCollectService.selectListBymemberId(jwtInfo.getId());
        return R.ok().data("items",list);
    }

    @ApiOperation(value = "取消收藏课程")
    @DeleteMapping("auth/remove/{courseId}")
    public R remove(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId,
            HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
     boolean result =     courseCollectService.removeCourseCollect(courseId,jwtInfo.getId());
     if (result){
         return R.ok().message("已取消");
     } else {
         return R.error().message("取消失败");
     }
    }

}