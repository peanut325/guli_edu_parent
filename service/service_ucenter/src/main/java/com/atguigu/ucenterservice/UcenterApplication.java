package com.atguigu.ucenterservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.atguigu.ucenterservice.mapper")
@ComponentScan("com.atguigu")   // 扫描common工程下的配置文件
@EnableDiscoveryClient          // 开启nacos注册
@EnableFeignClients             // 开启feign代理
public class UcenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UcenterApplication.class , args);
    }
}
