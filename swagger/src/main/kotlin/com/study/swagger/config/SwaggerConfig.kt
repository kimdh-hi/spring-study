package com.study.swagger.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.Contact
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@EnableSwagger2
@Configuration
class SwaggerConfig {

    @Bean
    fun postApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .groupName("groupName")
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.study.swagger.controller")) // rest-controller 스캔범위 지정
            .paths(PathSelectors.any()) // apis에서 선택된 API 중 특정 경로를 지정
            .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("POSTS API TITLE - apiInfo title")
            .description("[POSTS API] DESCRIPTION - apiInfo description")
            .contact(Contact("apiInfo contact - name", "apiInfo contact - url", "apiInfo contact - email"))
            .build()
    }


}