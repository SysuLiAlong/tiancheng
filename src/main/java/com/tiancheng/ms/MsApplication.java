package com.tiancheng.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(scanBasePackages = "com.tiancheng.ms")
@MapperScan(value = "com.tiancheng.ms.dao.mapper")
@EnableConfigurationProperties
public class MsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsApplication.class, args);
    }
}
