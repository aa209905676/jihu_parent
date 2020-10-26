package com.atguigu.guli.service.edu.controller.API;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

//@CrossOrigin
@Api(tags="课程分类")
@RestController
@RequestMapping("/api/edu/subject")
public class ApiSubjectController {
    @Resource
    private SubjectService subjectService;

    @ApiOperation("嵌套数据列表")
    @GetMapping("nested-list")
    public R nestedList(){
        List<Subject> subjectList = subjectService.nestList();
        return R.ok().data("items",subjectList);
   }

}
