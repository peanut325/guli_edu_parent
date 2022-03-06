package com.atguigu.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.atguigu")   // 扫描common工程下的配置文件
@EnableDiscoveryClient          // 开启nacos注册
@EnableFeignClients             // 开启feign代理
public class AliVodApplication {
    public static void main(String[] args) {
        SpringApplication.run(AliVodApplication.class, args);
    }
}
