package com.atguigu.oss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author LcY
 * @create 2022-09-03 10:42
 */
@RestController // 交给Spring管理并返回json数据
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {
    @Autowired
    private OssService ossService;

    @PostMapping
    public R uploadOssFile(MultipartFile file) {
        // 获取上传文件 MultiPartFile
        // 让方法返回上传到oss的路径，把这个路径存到数据库中
        String url = ossService.uploadFileAvatar(file);
        return R.ok().data("url", url);
    }
}
