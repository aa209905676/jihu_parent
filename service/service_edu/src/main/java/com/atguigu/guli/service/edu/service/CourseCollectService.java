package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.CourseCollect;
import com.atguigu.guli.service.edu.entity.vo.CourseCollectVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程收藏 服务类
 * </p>
 *
 * @author wjh
 * @since 2020-08-13
 */
public interface CourseCollectService extends IService<CourseCollect> {

    boolean isCollect(String courseId, String id);

    void saveCourseCollect(String courseId, String memberId);

    List<CourseCollectVo> selectListBymemberId(String memberId);


    boolean removeCourseCollect(String courseId, String id);
}
