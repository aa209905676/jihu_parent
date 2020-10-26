package com.atguigu.guli.service.edu.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelProperty;
@Data
public class excelSubjectData {
    @ExcelProperty(value = "一级分类")
    private String levelOneTitle;

    @ExcelProperty(value = "二级分类")
    private String levelTwoTitle;
}
