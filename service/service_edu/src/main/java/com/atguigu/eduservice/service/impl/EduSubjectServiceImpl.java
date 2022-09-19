package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.One;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-03
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    // 添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {
        try {
            // 获取文件输入流
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, SubjectData.class, new SubjectExcelListener(eduSubjectService)).sheet().doRead();  // 监听器中的invoke方法对每行数据进行操作
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // 树型结构的课程分类
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        // 查询所有一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id", "0");
//        List<EduSubject> oneSubjectList = this.list(wrapperOne);
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        // 查询所有二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id", "0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);
        // 封装一级分类
        List<OneSubject> finalSubjectList = new ArrayList<>();
        for (EduSubject oneSubject : oneSubjectList) {
            OneSubject tempOneSubject = new OneSubject();

            String id = oneSubject.getId();
            String title = oneSubject.getTitle();

            tempOneSubject.setId(id);
            tempOneSubject.setTitle(title);

            // 封装二级分类
            for (EduSubject twoSubject : twoSubjectList) {
                if (twoSubject.getParentId().equals(id)) {
                    TwoSubject tempTwoSubject = new TwoSubject();
//                    tempTwoSubject.setId(twoSubject.getId());
//                    tempTwoSubject.setTitle(twoSubject.getTitle());
                    BeanUtils.copyProperties(twoSubject, tempTwoSubject);
                    tempOneSubject.getChildren().add(tempTwoSubject);
                }
            }

            finalSubjectList.add(tempOneSubject);
        }

        return finalSubjectList;
    }
}
