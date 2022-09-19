package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author LcY
 * @create 2022-09-10 23:25
 */
@Component
public class VodFileDegradeFeignClient implements VodClient{
    // 熔断后执行
    @Override
    public R removeAliyunVideo(String id) {
        return R.error().message("删除视频出错了");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错了");
    }
}
