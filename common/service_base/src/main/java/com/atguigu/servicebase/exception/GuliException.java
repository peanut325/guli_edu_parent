package com.atguigu.servicebase.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuliException extends RuntimeException{
    private Integer code;   // 异常状态码
    private String msg;     // 异常信息
}
