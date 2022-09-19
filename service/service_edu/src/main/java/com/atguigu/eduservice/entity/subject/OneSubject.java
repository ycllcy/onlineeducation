package com.atguigu.eduservice.entity.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LcY
 * @create 2022-09-04 22:08
 */
@Data
public class OneSubject {
    private String id;
    private String title;

    // 一个一级分类中有多个二级分类
    private List<TwoSubject> children = new ArrayList<>();
}

