package com.atguigu.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class TeacherQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Integer level;
    private String joinDateBegin;
    private String joinDateEnd;
}