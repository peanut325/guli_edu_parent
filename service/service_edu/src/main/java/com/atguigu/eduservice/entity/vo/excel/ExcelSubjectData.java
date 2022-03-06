package com.atguigu.eduservice.entity.vo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 *  对应excel的实体类
 */
@Data
public class ExcelSubjectData {

    // 对应的为excel的第一列
    @ExcelProperty(index = 0)
    private String oneSubjectName;

    // 对应的为excel的第二列
    @ExcelProperty(index = 1)
    private String twoSubjectName;

}
