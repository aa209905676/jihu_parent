package com.atguigu.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Resource
    private OssProperties ossProperties;

    @Override
    public String upload(InputStream inputStream, String module, String originalFielName) {
        // 读取配置信息
        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();
        String bucketname = ossProperties.getBucketname();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);
         // 判断bucketname 是否存在
        if (!ossClient.doesBucketExist(bucketname)){
            ossClient.createBucket(bucketname);
            // 设置读权限
            ossClient.setBucketAcl(bucketname, CannedAccessControlList.PublicRead);

        }
        // 构建objectname， 文件路径 avatat/2020/01/07/文件名.jpg
         // 随机取个uuid
        String fileName = UUID.randomUUID().toString();
          // 格式化日期
        String  folder =  new DateTime().toString("yyyy/MM/dd");
        // 取文件的后缀名
        String fileExtension = originalFielName.substring(originalFielName.lastIndexOf("."));
         String key = module+"/"+folder+"/"+fileName+fileExtension;
       // 上传文件流。
        ossClient.putObject(bucketname, key, inputStream);

       // 关闭OSSClient。
        ossClient.shutdown();
//         返回一个URL
        return "https://"+bucketname+"."+endpoint+"/"+key;
    }

    @Override
    public void removeFile(String url) {
        // 读取配置信息
        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();
        String bucketname = ossProperties.getBucketname();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);
        // 删除文件
//        String host="https://"+bucketname+"."+endpoint+"/";
        String host = "https://" + bucketname + "." + endpoint + "/";
         // 删掉host，从host后面开始取值
        String substring = url.substring(host.length());
        ossClient.deleteObject(bucketname,substring);
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
