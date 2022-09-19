package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-09-05
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService eduCourseService;

    // 课程列表 基本查询
    // TODO 完善条件查询带分页的效果
    @GetMapping
    public R getCourseList() {
        List<EduCourse> courseList = eduCourseService.list(null);
        return R.ok().data("list", courseList);
    }
    // 添加课程基本信息的方法
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        // 返回添加课程后的id，为了后续添加章节目录使用
        String id = eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    // 根据课程id查询课程信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId) {
        CourseInfoVo courseInfoVo = eduCourseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo", courseInfoVo);
    }

    // 修改课程信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        eduCourseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    // 根据课程id查询课程确认信息
    @GetMapping("getCoursePublishInfo/{id}")
    public R getCoursePublishInfo(@PathVariable String id) {
        CoursePublishVo coursePublishVo = eduCourseService.coursePublishInfo(id);
        return R.ok().data("publishCourse", coursePublishVo);
    }

    // 课程最终发布
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id) {
        EduCourse course = new EduCourse();
        course.setId(id);
        course.setStatus(EduCourse.COURSE_NORMAL);
        eduCourseService.updateById(course);
        return R.ok();
    }

    // 根据id删除课程
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId) {

        eduCourseService.removeCourse(courseId);
        return R.ok();
    }
}

