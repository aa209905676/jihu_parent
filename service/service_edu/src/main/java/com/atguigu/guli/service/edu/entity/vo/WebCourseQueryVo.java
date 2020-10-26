package com.atguigu.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WebCourseQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String subjectParentId;
    private String subjectId;
    private String buyCountSort;
    private String gmtCreateSort;
    private String priceSort;

    private Integer type; //价格正序：1，价格倒序：2
}