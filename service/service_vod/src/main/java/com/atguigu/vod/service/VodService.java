package com.atguigu.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author LcY
 * @create 2022-09-10 10:10
 */
public interface VodService {

    String uploadVideoAliyun(MultipartFile file);

    // 删除阿里云中多个视频
    void removeMoreAliyunVideo(List<String> videoIdList);
}
