package com.atguigu.eduservice.entity.chapter;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LcY
 * @create 2022-09-06 19:26
 */
@Data
public class ChapterVo {
    private String id;
    private String title;

    private List<VideoVo> children = new ArrayList<>();
}
