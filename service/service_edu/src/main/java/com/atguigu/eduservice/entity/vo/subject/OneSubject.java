package com.atguigu.eduservice.entity.vo.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *  一级课程分类
 */
@Data
public class OneSubject {

    private String id;
    private String title;

    private List<TwoSubject> children = new ArrayList<>();
}
