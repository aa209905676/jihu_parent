package com.atguigu.guli.service.oss.service;

import java.io.InputStream;

public interface FileService {
    /**
     *阿里云oss 文件商场
     * @param inputStream 输入流
     * @param module 文件夹名称
     * @param originalFielName 原始文件名
     * @return 文件的url地址
     */
    String upload(InputStream inputStream,String module,String originalFielName);

    /**
     * 阿里云oss文件删除
     * @param url 文件url
     */
    void removeFile(String url);
}
