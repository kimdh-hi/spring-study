package com.study.swagger.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig {

    @Bean
    fun postApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .groupName("posts")
            .apiInfo(apiInfo())
            .select()
            .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("POSTS API TITLE")
            .description("[POSTS API] DESCRIPTION")
            .contact(Contact("POSTS API", "https://my-contact.com/", "zbeld123@gmail.com"))
            .build()
    }
}