package com.easy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.easy.mapper")
public class RunDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunDemoApplication.class, args);
    }

}

