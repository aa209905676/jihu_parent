package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.CourseCollect;
import com.atguigu.guli.service.edu.entity.vo.CourseCollectVo;
import com.atguigu.guli.service.edu.mapper.CourseCollectMapper;
import com.atguigu.guli.service.edu.service.CourseCollectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 课程收藏 服务实现类
 * </p>
 *
 * @author wjh
 * @since 2020-08-13
 */
@Service
public class CourseCollectServiceImpl extends ServiceImpl<CourseCollectMapper, CourseCollect> implements CourseCollectService {

    @Override
    public boolean isCollect(String courseId, String memberId) {
        QueryWrapper<CourseCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId).eq("member_id",memberId);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    @Override
    public void saveCourseCollect(String courseId, String memberId) {
        // 未收藏则收藏
        if (!this.isCollect(courseId,memberId)){
            CourseCollect courseCollect = new CourseCollect();
            courseCollect.setCourseId(courseId);
            courseCollect.setMemberId(memberId);
            this.save(courseCollect);
        }
    }

    @Override
    public List<CourseCollectVo> selectListBymemberId(String memberId) {
        return baseMapper.selectPageByMmeberId(memberId);

    }

    @Override
    public boolean removeCourseCollect(String courseId, String memberId) {
      if (this.isCollect(courseId,memberId)){
          QueryWrapper<CourseCollect> queryWrapper = new QueryWrapper<>();
          queryWrapper.eq("course_id",courseId).eq("member_id",memberId);
          return this.remove(queryWrapper);
      }
      return false;
    }

}
