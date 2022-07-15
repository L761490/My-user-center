package com.lihao.myusercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lihao.myusercenter.mapper")
public class MyUserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyUserCenterApplication.class, args);
    }

}
