package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-05
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    // 注入VodClient
    @Autowired
    private VodClient vodClient;

    // 根据课程id删除对应小节
    @Override
    public void removeVideoByCourseId(String courseId) {
        // 删除阿里云中的视频
        // 根据课程id查询所有对应的视频id
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.select("video_source_id");
        List<EduVideo> eduVideos = baseMapper.selectList(wrapper);

        List<String> videoIds = new ArrayList<>();
        for (EduVideo eduVideo : eduVideos) {
            String videoId = eduVideo.getVideoSourceId();
            if (!StringUtils.isEmpty(videoId)) {
                videoIds.add(videoId);
            }
        }

        if (videoIds.size() > 0) {
            // 根据多个视频ID删除视频
            vodClient.deleteBatch(videoIds);
        }

        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id", courseId);
        baseMapper.delete(videoWrapper);
    }
}
