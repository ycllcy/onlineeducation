package com.atguigu.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author LcY
 * @create 2022-09-03 10:42
 */
public interface OssService {
    // 上传头像到oss
    String uploadFileAvatar(MultipartFile file);
}
