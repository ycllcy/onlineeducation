package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2022-09-05
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    public CoursePublishVo getCoursePublishInfo(String courseId);

    // 根据课程id查询课程详细信息
    CourseWebVo getBaseCourseInfo(String courseId);
}
