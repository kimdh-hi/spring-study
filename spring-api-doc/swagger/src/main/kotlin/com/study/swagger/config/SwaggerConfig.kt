package com.study.swagger.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@Configuration
class SwaggerConfig {

  @Bean
  fun openAPI(): OpenAPI {
    return OpenAPI()
      .info(info())
      .components(
        Components().addSecuritySchemes(
          "SEC",
          SecurityScheme().type(SecurityScheme.Type.APIKEY).`in`(SecurityScheme.In.HEADER).name(HttpHeaders.AUTHORIZATION)
        )
      )
  }

  private fun info() = Info()
    .title("Title")
    .description("Description")
    .version("1.0")

}
