package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-08-28
 */
public interface EduTeacherService extends IService<EduTeacher> {
    // 返回分页查询讲师所有数据
    Map<String, Object> getTeacherFrontList(Page<EduTeacher> teacherPage);
}
