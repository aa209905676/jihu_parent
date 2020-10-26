package com.atguigu.guli.service.trade.feign.fallback;

import com.atguigu.guli.service.base.dto.CourseDto;

import com.atguigu.guli.service.trade.feign.EduCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EduCourseServiceFallBack implements EduCourseService {
    @Override
    public CourseDto getCourseDtoById(String courseId) {
        log.info("熔断保护");
        return null;
    }
}
