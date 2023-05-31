package com.dky;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Hello world!
 */
@SpringBootApplication
@MapperScan("com.dky.mapper")
@EnableScheduling
@EnableSwagger2
public class DkyBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(DkyBlogApplication.class,args);
    }
}
