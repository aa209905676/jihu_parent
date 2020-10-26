package com.atguigu.guli.service.vod.Service;

import com.aliyun.oss.ClientException;

import java.io.InputStream;
import java.util.List;

public interface VideoService {
    String uploadVideo(InputStream file,String originalFilename);

    void removeVideo(String videoId) throws ClientException, Exception;

    void removeVideoByIdList(List<String> videoIdList) throws com.aliyuncs.exceptions.ClientException;

    String getPlayAuth(String videoSourceId) throws com.aliyuncs.exceptions.ClientException;
}
