package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.Feign.ossFeignService;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.vo.TeacherQueryVo;
import com.atguigu.guli.service.edu.mapper.CourseMapper;
import com.atguigu.guli.service.edu.mapper.TeacherMapper;
import com.atguigu.guli.service.edu.service.CourseService;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author wjh
 * @since 2020-08-13
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Resource
    private ossFeignService ossFeignService;
    @Resource
    private CourseMapper courseMapper;
    @Override
    public IPage<Teacher> selectPage(Page<Teacher> pageParam, TeacherQueryVo teacherQueryVo) {
        //显示分页查询列表
//        1、排序：按照sort字段排序
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");

//        2、分页查询
        if(teacherQueryVo == null){
            return baseMapper.selectPage(pageParam, queryWrapper);
        }

//        3、条件查询
        String name = teacherQueryVo.getName();
        Integer level = teacherQueryVo.getLevel();
        String joinDateBegin = teacherQueryVo.getJoinDateBegin();
        String joinDateEnd = teacherQueryVo.getJoinDateEnd();

        if(!StringUtils.isEmpty(name)){
            queryWrapper.likeRight("name", name);
        }

        if(level != null){
            queryWrapper.eq("level", level);
        }

        if(!StringUtils.isEmpty(joinDateBegin)){
            queryWrapper.ge("join_date", joinDateBegin);
        }

        if(!StringUtils.isEmpty(joinDateEnd)){
            queryWrapper.le("join_date", joinDateEnd);
        }

        return baseMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public List<Map<String, Object>> selectNamwByList(String key) {
     // new 一个条件构造器
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
         // 查询name 列
        queryWrapper.select("name");
        queryWrapper.likeRight("name",key);
        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        return maps;

    }

    @Override
    public boolean removeAvatarById(String id) {

        // 根据id 获取讲师列表
        Teacher teacher = baseMapper.selectById(id);
        if (teacher != null){
            String avatar = teacher.getAvatar();
            if (!StringUtils.isEmpty(avatar)){
                R r = ossFeignService.removeFile(avatar);
                return r.getSuccess();
            }
        }
        return false;

    }

    @Override
    public Map<String, Object> selectTeacherInfoById(String id) {
         // 获取讲师信息和课程信息 ，然后一起存进去
        Teacher teacher = baseMapper.selectById(id);
        // 根据老师获取他的课程信息
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id",teacher.getId());
        List<Course> courseList = courseMapper.selectList(queryWrapper);

        Map<String , Object> map = new HashMap<>();
        map.put("teacher",teacher);
        map.put("courseList",courseList);
        return map;
    }

    @Cacheable(value = "index", key = "'selectHotTeacher'")
    @Override
    public List<Teacher> selectHotTeacher() {
            QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sort");
        queryWrapper.last("limit 4");
        return baseMapper.selectList(queryWrapper);
    }
}

