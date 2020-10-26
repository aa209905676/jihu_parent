package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.Feign.ossFeignService;
import com.atguigu.guli.service.edu.entity.*;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.*;
import com.atguigu.guli.service.edu.mapper.*;
import com.atguigu.guli.service.edu.service.CourseDescriptionService;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jdk.internal.dynalink.linker.LinkerServices;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author wjh
 * @since 2020-08-13
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Resource
    private CourseDescriptionMapper courseDescriptionMapper;

    @Resource
    private ossFeignService ossFeignService;

    @Resource
    private VideoMapper videoMapper;
    @Resource
    private ChapterMapper chapterMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private CourseCollectMapper courseCollectMapper;
    @Resource
    private TeacherMapper teacherMapper;

    // 只要发生异常 则回滚 注意：需要mp开启事物的注解
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        // 保存course
        Course course = new Course();
        /** 把 元对象和目的对象 里面同名的属性 copy过来
         * 就是赋值
         */
        BeanUtils.copyProperties(courseInfoForm,course);
        course.setStatus(course.COURSE_DRAFT);
        baseMapper.insert(course);


        // 保存coursedesription
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.insert(courseDescription);

        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoById(String id) {
        // 从course 获取数据
        Course course = baseMapper.selectById(id);
        if (course == null){
            return null;
        }
        // 通过id获取description
        CourseDescription courseDescription = courseDescriptionMapper.selectById(id);
        // 组装
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course,courseInfoForm);
        courseInfoForm.setDescription(courseDescription.getDescription());

        return courseInfoForm;
    }

    @Override
    public void updateCourseInfoById(CourseInfoForm courseInfoForm) {
        // 更新course
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm,course);
        baseMapper.updateById(course);


        // 更新coursedesription
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.updateById(courseDescription);

    }

    @Override
    public IPage<CourseVo> selectPage(Long page, Long limit, CourseQueryVo courseQueryVo) {
        QueryWrapper<CourseVo> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.orderByDesc("c.gmt_create");

        String title = courseQueryVo.getTitle();
        String teacherId = courseQueryVo.getTeacherId();
        String subjectParentId = courseQueryVo.getSubjectParentId();
        String subjectId = courseQueryVo.getSubjectId();
        // 查询条件
        if (!StringUtils.isEmpty(title)){
            QueryWrapper.like("c.title",title);
        }
        if (!StringUtils.isEmpty(teacherId) ) {
            QueryWrapper.eq("c.teacher_id", teacherId);
        }

        if (!StringUtils.isEmpty(subjectParentId)) {
            QueryWrapper.eq("c.subject_parent_id", subjectParentId);
        }

        if (!StringUtils.isEmpty(subjectId)) {
            QueryWrapper.eq("c.subject_id", subjectId);
        }
//     分页
        Page<CourseVo> pageParam = new Page<>(page, limit);
        // 结果列表  只需要在mapper层传入封装的好的分页组件即可，sql分页组件的过程由mp插件完成
       List<CourseVo>  records = baseMapper.selectPageByCourseQueryVo(pageParam,QueryWrapper);
       pageParam.setRecords(records);
       return pageParam;


    }
//   删除封面
    @Override
    public Boolean removeCoverById(String id) {
// 根据id 获取讲师列表
        Course course = baseMapper.selectById(id);
        if (course != null){
            String avatar = course.getCover();
            if (!StringUtils.isEmpty(avatar)){
                R r = ossFeignService.removeFile(avatar);
                return r.getSuccess();
            }
        }
        return false;
    }
@Transactional(rollbackFor   = Exception.class)
    @Override
    public boolean removeCourseById(String id) {
        // 删除视频
        QueryWrapper<Video> videoqueryWrapper = new QueryWrapper<>();
        videoqueryWrapper.eq("course_id",id);
        videoMapper.delete(videoqueryWrapper);
        // 删除收藏
        QueryWrapper<CourseCollect> CourseCollectQueryWrapper = new QueryWrapper<>();
        CourseCollectQueryWrapper.eq("course_id",id);
        courseCollectMapper.delete(CourseCollectQueryWrapper);
        // 删除评论
        QueryWrapper<Comment> CommentQueryWrapper = new QueryWrapper<>();
        CommentQueryWrapper.eq("course_id",id);
        commentMapper.delete(CommentQueryWrapper);
        // 删除章节
        QueryWrapper<Chapter> ChapterQueryWrapper = new QueryWrapper<>();
        ChapterQueryWrapper.eq("course_id",id);
        chapterMapper.delete(ChapterQueryWrapper);
        //课程详情：course_description
        courseDescriptionMapper.deleteById(id);
        //课程信息：course
        return this.removeById(id);

    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {
        return baseMapper.selectCoursePublishVoById(id);
    }

    @Override
    public boolean publishCourseById(String id) {
        Course course = new Course();
        course.setId(id);
        course.setStatus(Course.COURSE_NORMAL);
        return this.updateById(course);
    }

    @Override
    public List<Course> webSelectList(WebCourseQueryVo webCourseQueryVo) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",Course.COURSE_NORMAL);
       // 有则获取
        if (!StringUtils.isEmpty(webCourseQueryVo.getSubjectId())){
            queryWrapper.eq("subject_parent_id",webCourseQueryVo.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(webCourseQueryVo.getSubjectId())) {
            queryWrapper.eq("subject_id", webCourseQueryVo.getSubjectId());
        }

        if (!StringUtils.isEmpty(webCourseQueryVo.getBuyCountSort())) {
            queryWrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(webCourseQueryVo.getGmtCreateSort())) {
            queryWrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(webCourseQueryVo.getPriceSort())) {
            if (webCourseQueryVo.getType() == null || webCourseQueryVo.getType() == 1){
                queryWrapper.orderByAsc("price");
            }else{
                queryWrapper.orderByDesc("price");
            }

        }

        return baseMapper.selectList(queryWrapper);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public WebCourseVo selectWebCourseVoById(String id) {
        // 跟新课程浏览量
        Course course = baseMapper.selectById(id);
        course.setViewCount(course.getViewCount() + 1);
        // 获取课程信息
        return baseMapper.selectWebCourseVoById(id);
    }

    @Cacheable(value = "index", key = "'selectHotCourse'")
    @Override
    public List<Course> selectHotCourse() {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("view_count");
        queryWrapper.last("limit 8");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public CourseDto getCourseDtoById(String courseId) {
        // mapper
        return baseMapper.selectCourseDtoById(courseId);

//        Course course = baseMapper.selectById(courseId);
//     // 获得教师id
//        String teacherId = course.getTeacherId();
//        // 查询教师id
//        Teacher teacher = teacherMapper.selectById(teacherId);
//        // 组装数据
//        CourseDto courseDto = new CourseDto();
//        courseDto.setId(course.getId());
//        courseDto.setCover(course.getCover());
//        courseDto.setPrice(course.getPrice());
//        courseDto.setTitle(course.getTitle());
//        courseDto.setTeacherName(teacher.getName());
//
//        return courseDto;
    }
}
