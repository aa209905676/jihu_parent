package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.Feign.VodMediaService;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author wjh
 * @since 2020-08-13
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Resource
    private VodMediaService vodMediaService;

    @Override
    public void removeVideo(String id) {
        Video video = baseMapper.selectById(id);
        String videoSourceId = video.getVideoSourceId();
        vodMediaService.removeVideo(videoSourceId);
    }

    @Override
    public void removeMediaVideoById(String chapterId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("chapter_id",chapterId);
        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        ArrayList<String > arrayList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            String sourceId = (String) map.get("video_source_id");
            arrayList.add(sourceId);
        }
        vodMediaService.removeVideoByIdList(arrayList);
    }

    @Override
    public void removeMediaVideoByCourseId(String id) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("course_id",id);
        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        List<String> videoSourceIdList = this.getVideoSourceIdList(maps);
        vodMediaService.removeVideoByIdList(videoSourceIdList);
    }

    /**
     *  辅助方法 获取阿里云的视频id
     * @param maps
     * @return
     */
    private List<String>getVideoSourceIdList(List<Map<String ,Object>> maps){
        ArrayList<String > arrayList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            String sourceId = (String) map.get("video_source_id");
            arrayList.add(sourceId);
        }
        return arrayList;
    }
}
