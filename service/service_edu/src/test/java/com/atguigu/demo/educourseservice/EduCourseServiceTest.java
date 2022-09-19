package com.atguigu.demo.educourseservice;

import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.service.EduCourseService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author LcY
 * @create 2022-09-06 23:23
 */
public class EduCourseServiceTest {
    @Autowired
    private EduCourseService eduCourseService;

    @Test
    public void testGetCourseInfoVo() {
        CourseInfoVo courseInfo = eduCourseService.getCourseInfo("18");
        System.out.println(courseInfo);
    }
}
