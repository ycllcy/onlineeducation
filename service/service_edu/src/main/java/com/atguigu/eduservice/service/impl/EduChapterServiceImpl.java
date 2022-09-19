package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-05
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    private EduVideoService eduVideoService;
    // 课程大纲列表，根据课程id进行查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        List<EduChapter> chapters = baseMapper.selectList(wrapper);

        List<ChapterVo> chapterVoList = new ArrayList<>();

        for (EduChapter chapter : chapters) {   // 这种遍历方式相当于是创建了一个对象，而不是原来对象的引用
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);

            QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
            videoWrapper.eq("chapter_id", chapterVo.getId());
            List<EduVideo> videoList = eduVideoService.list(videoWrapper);

            List<VideoVo> videoVos = new ArrayList<>();
            for (EduVideo video : videoList) {
                VideoVo videoVo = new VideoVo();
                BeanUtils.copyProperties(video, videoVo);
                videoVos.add(videoVo);
            }

            chapterVo.setChildren(videoVos);

            chapterVoList.add(chapterVo);
        }
        return chapterVoList;
    }

    // 删除章节的方法
    @Override
    public boolean deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
//        List<EduVideo> videoList = eduVideoService.list(wrapper);
        int count = eduVideoService.count(wrapper);

        if (count > 0) {    // 查询出小节，不进行删除
            throw  new GuliException(20001, "不能删除");
        } else {
            int res = baseMapper.deleteById(chapterId);
            return res > 0;
        }
    }

    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id", courseId);
        baseMapper.delete(chapterWrapper);
    }
}
