package com.atguigu.commonutils.dto;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "TeacherInfo信息对象用于订单和评论", description = "传输TeacherInfo信息")
public class TeacherInfoDO {

    @ApiModelProperty(value = "讲师姓名")
    private String name;

}
