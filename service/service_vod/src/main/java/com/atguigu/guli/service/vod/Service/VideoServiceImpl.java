package com.atguigu.guli.service.vod.Service;

import com.alibaba.excel.util.StringUtils;
import com.aliyun.oss.ClientException;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuLiExcpetion;
import com.atguigu.guli.service.vod.util.AliyunVodSDKUtils;
import com.atguigu.guli.service.vod.util.VodProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService{

    @Resource
    private VodProperties vodProperties;
    @Override
    public String uploadVideo(InputStream inputStream, String originalFilename) {

        String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));

        UploadStreamRequest request = new UploadStreamRequest(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret(),
                title, originalFilename, inputStream);

//        /* 模板组ID(可选) */
//        request.setTemplateGroupId(vodProperties.getTemplateGroupId());
//        /* 工作流ID(可选) */
//      request.setWorkflowId(vodProperties.getWorkflowId());

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);

        String videoId = response.getVideoId();
        //没有正确的返回videoid则说明上传失败
        if(StringUtils.isEmpty(videoId)){
            log.error("阿里云上传失败：" + response.getCode() + " - " + response.getMessage());
            throw new GuLiExcpetion(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }

        return videoId;
    }

    @Override
    public void removeVideo(String videoId) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret());

        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(videoId);
        try {
            client.getAcsResponse(request);
        } catch (com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeVideoByIdList(List<String> videoIdList) throws com.aliyuncs.exceptions.ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret());

        DeleteVideoRequest request = new DeleteVideoRequest();

        int size = videoIdList.size();
        // 存id容器
        StringBuffer idListStr = new StringBuffer();
        // 取id
        for (int i = 0;i< size;i++){
            idListStr.append(videoIdList.get(i));
            // 判断是否满20个 是则删除 ，因为最多只能删除20个
            if (i == size - 1 || i % 20 == 19 ){
                log.info("idList"+idListStr.toString());
                request.setVideoIds(idListStr.toString());
                client.getAcsResponse(request);
                 // 如果size没有超过20
            }else if ( i % 20 <19){
                idListStr.append(",");
            }
        }
    }

    @Override
    public String getPlayAuth(String videoSourceId) throws com.aliyuncs.exceptions.ClientException {
        //初始化client对象
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret());
        //创建请求对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest ();
        request.setVideoId(videoSourceId);

        //获取响应
        GetVideoPlayAuthResponse response = client.getAcsResponse(request);

        return response.getPlayAuth();

    }
}