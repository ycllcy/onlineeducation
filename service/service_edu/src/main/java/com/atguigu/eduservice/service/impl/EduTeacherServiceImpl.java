package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-08-28
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {
    // 返回分页查询讲师所有数据
    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> teacherPage) {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        // 分页查询的数据封装到teacherPage中
        baseMapper.selectPage(teacherPage, wrapper);

        long total = teacherPage.getTotal();
        long pages = teacherPage.getPages();
        long size = teacherPage.getSize();
        long current = teacherPage.getCurrent();
        boolean hasPrevious = teacherPage.hasPrevious();
        boolean hasNext = teacherPage.hasNext();
        List<EduTeacher> records = teacherPage.getRecords();

        Map<String, Object> map = new HashMap<>();

        map.put("total", total);
        map.put("pages", pages);
        map.put("size", size);
        map.put("current", current);
        map.put("hasPrevious", hasPrevious);
        map.put("hasNext", hasNext);
        map.put("items", records);

        return map;
    }
}
