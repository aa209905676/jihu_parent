package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.vo.TeacherQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author wjh
 * @since 2020-08-13
 */
public interface TeacherService extends IService<Teacher> {

    IPage<Teacher> selectPage(Page<Teacher> pageParam, TeacherQueryVo teacherQueryVo);


    List<Map<String, Object>> selectNamwByList(String key);

    boolean removeAvatarById(String  id);

    /**
     * 根据讲师id获取讲师详情页数据
     * @param id
     * @return
     */
    Map<String, Object> selectTeacherInfoById(String id);

    List<Teacher> selectHotTeacher();
}
