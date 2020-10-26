package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.entity.vo.VideoVo;
import com.atguigu.guli.service.edu.mapper.ChapterMapper;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {
    @Resource
    private VideoMapper videoMapper;

   @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeChapterById(String id) {
        // 删除视频
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",id);
        videoMapper.delete(queryWrapper);

        return this.removeById(id);
    }

    @Override
    public List<ChapterVo> nestedList(String courseId) {

        List<ChapterVo> chapterVoList = new ArrayList<>();

        //获取章信息
        QueryWrapper<Chapter> queryWrapperChapter = new QueryWrapper<>();
        queryWrapperChapter.eq("course_id", courseId);
        queryWrapperChapter.orderByAsc("sort", "id");
        List<Chapter> chapterList = baseMapper.selectList(queryWrapperChapter);

        //获取课时信息
        QueryWrapper<Video> queryWrapperVideo = new QueryWrapper<>();
        queryWrapperVideo.eq("course_id", courseId);
        queryWrapperVideo.orderByAsc("sort", "id");
        List<Video> videoList = videoMapper.selectList(queryWrapperVideo);

        //填充列表数据：Chapter列表
        for (int i = 0; i < chapterList.size(); i++) {
            Chapter chapter = chapterList.get(i);

            //创建ChapterVo对象
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);
            chapterVoList.add(chapterVo);

            //填充列表数据：Video列表
            List<VideoVo> videoVoList = new ArrayList<>();
            for (int j = 0; j < videoList.size(); j++) {
                Video video = videoList.get(j);
                if(chapter.getId().equals(video.getChapterId())){

                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video, videoVo);
                    videoVoList.add(videoVo);
                }
            }

            chapterVo.setChildren(videoVoList);
        }

        return chapterVoList;
    }
}
