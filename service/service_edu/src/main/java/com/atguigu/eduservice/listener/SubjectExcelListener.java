package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author LcY
 * @create 2022-09-03 23:55
 */

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    public EduSubjectService eduSubjectService;

    public SubjectExcelListener(){

    }

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData == null) {
            throw new GuliException(20001, "文件数据为空");
        }

        // 判断一级分类是否重复添加
        EduSubject existOneSubject = this.existOneSubject(this.eduSubjectService, subjectData.getOneSubjectName());
        if (existOneSubject == null) {  // 没有相同一级分类，进行添加
            existOneSubject = new EduSubject();
            existOneSubject.setTitle(subjectData.getOneSubjectName());
            existOneSubject.setParentId("0");

            this.eduSubjectService.save(existOneSubject); // MyBatis-Plus也会在实体类中自动填充对应属性
        }

        // 判断二级分类是否重复添加
        String pid = existOneSubject.getId();
        EduSubject existTwoSubject = existTwoSubject(this.eduSubjectService, subjectData.getTwoSubjectName(), pid);
        if (existTwoSubject == null) {
            existTwoSubject = new EduSubject();
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());
            existTwoSubject.setParentId(pid);

            this.eduSubjectService.save(existTwoSubject);
        }
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
    }

    // 判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService eduSubjectService, String name) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", "0");

        return eduSubjectService.getOne(wrapper);
    }

    // 判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService eduSubjectService, String name, String pid) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", pid);

        return eduSubjectService.getOne(wrapper);
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
