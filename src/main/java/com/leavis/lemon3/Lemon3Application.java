package com.leavis.lemon3;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@ServletComponentScan("com.leavis.lemon3.filter")
@MapperScan("com.leavis.lemon3.mapper")
@SpringBootApplication
@EnableCaching
public class Lemon3Application {

    public static void main(String[] args) {
        SpringApplication.run(Lemon3Application.class, args);
    }

}
