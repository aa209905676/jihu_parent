package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.Feign.ossFeignService;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.vo.TeacherQueryVo;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author wjh
 * @since 2020-08-13
 */
//@CrossOrigin //允许跨域
@Api(tags = "讲师管理")
@RestController
@RequestMapping("/admin/edu/teacher")
@Slf4j
public class TeacherControllerAdmin {

    @Resource
    private TeacherService teacherService;

    @Resource
    private ossFeignService ossFeignService;

    @ApiOperation(value = "所有讲师列表", notes = "详细信息")
    @GetMapping("test")
    public R test() {
        List<Teacher> list = teacherService.list();
        return R.ok().data("items", list);
    }

    @ApiOperation(value = "根据ID删除讲师", notes = "根据ID删除讲师，逻辑删除")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam(value = "讲师ID", required = true)
                            @PathVariable String id){

        // 删除讲师头像
        teacherService.removeAvatarById(id);
        // 删除讲师
        boolean result = teacherService.removeById(id);
        if(result){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("讲师分页列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码", required = true) @PathVariable Long page,
                      @ApiParam(value = "每页记录数", required = true) @PathVariable Long limit,
                      @ApiParam("讲师列表查询对象") TeacherQueryVo teacherQueryVo){

        Page<Teacher> pageParam = new Page<>(page, limit);
        IPage<Teacher> pageModel = teacherService.selectPage(pageParam, teacherQueryVo);
        List<Teacher> records = pageModel.getRecords();
        long total = pageModel.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation("新增讲师")
    @PostMapping("save")
    public R save(@ApiParam("讲师对象") @RequestBody Teacher teacher){
        teacherService.save(teacher);
        return R.ok().message("保存成功");
    }

    @ApiOperation("更新讲师")
    @PutMapping("update")
    public R updateById(@ApiParam("讲师对象") @RequestBody Teacher teacher){
        boolean result = teacherService.updateById(teacher);
        if(result){
            return R.ok().message("修改成功");
        }else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id获取讲师信息")
    @GetMapping("get/{id}")
    public R getById(@ApiParam("讲师对象") @PathVariable String id){
        Teacher teacher = teacherService.getById(id);
        if(teacher != null){
            return R.ok().data("item", teacher);
        }else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation(value = "根据ID列表删除讲师")
    @DeleteMapping("batch-remove")
    public R removeByIdList(@ApiParam(value = "讲师ID列表", required = true)
                            // 根据json取值
                            @RequestBody List<String> idList){

        boolean result = teacherService.removeByIds(idList);
        if(result){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("数据不存在");
        }
    }
    @ApiOperation(value = "通过关键字查询讲师列表")
    @GetMapping("list/name/{key}")
    public R selectNamwBykey(
            @ApiParam(value = "关键字",readOnly = true)
            @PathVariable String key
    ) {
        List<Map<String ,Object>> nameList = teacherService.selectNamwByList(key);
        return R.ok().data("nameList", nameList);
    }

    @ApiOperation("测试服务调用")
    @GetMapping("testOss")
    public R testOss(){
        ossFeignService.test();
        return R.ok();
    }

    @ApiOperation("测试并发")
    @GetMapping("test_concurrent")
    public R testConcurrent(){
        log.info("test_concurrent");
        return R.ok();
    }

    @GetMapping("/message1")
    public String message1() {
        return "message1";
    }

    @GetMapping("/message2")
    public String message2() {
        return "message2";
    }
}

