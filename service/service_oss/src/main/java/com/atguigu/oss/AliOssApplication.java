package com.atguigu.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) // 不用配置数据库，排除数据库的自动装配
@ComponentScan("com.atguigu")   // 扫描common工程下的配置文件
@EnableDiscoveryClient          // 开启nacos注册
public class AliOssApplication {
    public static void main(String[] args) {
        SpringApplication.run(AliOssApplication.class , args);
    }
}
