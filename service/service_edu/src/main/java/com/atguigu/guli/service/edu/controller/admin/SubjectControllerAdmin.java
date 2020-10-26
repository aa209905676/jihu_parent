package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.service.base.exception.GuLiExcpetion;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;


/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author wjh
 * @since 2020-08-13
 */
//@CrossOrigin //允许跨域
@Api(tags = "课程类别管理")
@RestController
@RequestMapping("/admin/edu/subject")
@Slf4j
public class SubjectControllerAdmin {
    @Resource
    private SubjectService subjectService;

    @PostMapping("import")
    @ApiOperation("execl导入课程分类")
    public R batchImport(
            @ApiParam(value = "excel文件",readOnly = true)
            @RequestParam("file")
            MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);
            return R.ok().message("文件导入成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuLiExcpetion(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }

    }

    @ApiOperation("嵌套数据列表")
        @GetMapping("nested-list")
    public  R nesteList(){
      List<Subject> subjectList =  subjectService.nestList();
      return R.ok().data("items",subjectList);
    }

}

