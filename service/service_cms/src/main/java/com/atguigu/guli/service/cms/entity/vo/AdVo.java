package com.atguigu.guli.service.cms.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
public class AdVo implements Serializable {
    private static final long serialVersionUID=1L;
    private String id;
    private String title;
    private Integer sort;
    private String type;
}