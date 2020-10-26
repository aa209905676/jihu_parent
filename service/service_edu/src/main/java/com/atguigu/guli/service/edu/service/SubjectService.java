package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author wjh
 * @since 2020-08-13
 */
public interface SubjectService extends IService<Subject> {
    void batchImport(InputStream inputStream);

    List<Subject> nestList();
}
