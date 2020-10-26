package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.Feign.VodMediaService;
import com.atguigu.guli.service.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.annotation.Resource;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author wjh
 * @since 2020-08-13
 */
public interface VideoService extends IService<Video> {


    void  removeVideo(String id);

   void   removeMediaVideoById(String chapterId);


    void removeMediaVideoByCourseId(String id);
}
