package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Subject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author wjh
 * @since 2020-08-13
 */
public interface SubjectMapper extends BaseMapper<Subject> {

    List<Subject> selectNestedListByParentId(String s);
}
