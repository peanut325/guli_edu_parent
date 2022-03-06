package com.atguigu.commonutils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResultEntity {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<String, Object>();

    // 无参构造为私有方法，不能直接new对象
    private ResultEntity() {
    }

    public static ResultEntity ok() {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setSuccess(true);
        resultEntity.setCode(ResultCode.SUCCESS);
        resultEntity.setMessage("成功");
        return resultEntity;
    }

    public static ResultEntity error() {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setSuccess(false);
        resultEntity.setCode(ResultCode.ERROR);
        resultEntity.setMessage("失败");
        return resultEntity;
    }

    public ResultEntity success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public ResultEntity message(String message) {
        this.setMessage(message);
        return this;
    }

    public ResultEntity code(Integer code) {
        this.setCode(code);
        return this;
    }

    // 保存数据
    public ResultEntity data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    // 保存数据
    public ResultEntity data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }
}


