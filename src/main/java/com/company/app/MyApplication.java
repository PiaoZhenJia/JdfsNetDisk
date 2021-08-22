package com.company.app;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PreDestroy;

@EnableSwagger2//swagger 接口文档
@SpringBootApplication(scanBasePackages = "com.company.**")//扫包层级
public class MyApplication {

    @PreDestroy
    public void onDestroy() {
        LoggerFactory.getLogger(MyApplication.class).info("优雅关闭");
    }

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
        LoggerFactory.getLogger(MyApplication.class).info("启动完成");
    }

}
