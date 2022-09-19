package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-09-05
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;

    // 注入vod-client
    @Autowired
    private VodClient vodClient;

    // 添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo) {
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    // 删除小节
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id) {
        // 根据小节id得到视频id
        EduVideo video = eduVideoService.getById(id);
        String videoSourceId = video.getVideoSourceId();

        if (!StringUtils.isEmpty(videoSourceId)) {
            // 根据视频id删除阿里云中的视频
            R result = vodClient.removeAliyunVideo(videoSourceId);

            if (result.getCode() == 20001) {
                throw new GuliException(20001, "删除视频失败，熔断器");
            }
        }

        eduVideoService.removeById(id);
        return R.ok();
    }

    // 修改小节
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo) {
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }


    // 根据id查询小节
    @GetMapping("getVideo/{id}")
    public R getVideo(@PathVariable String id) {
        EduVideo eduVideo = eduVideoService.getById(id);
        return R.ok().data("video", eduVideo);
    }
}

