package com.company.app.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Swagger2 配置类
 * http://localhost:8000/backend/swagger-ui/
 * 在这个版本中 上传文件时需使用@RequestPart注解
 *
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(requestHandler -> !requestHandler.groupName().equals("basic-error-controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MyApp接口文档")
                .description("Create By PiaoZhenJia")
                .contact(new Contact("PiaoZhenJia", null, null))
                .version("1.0")
                .build();
    }
}