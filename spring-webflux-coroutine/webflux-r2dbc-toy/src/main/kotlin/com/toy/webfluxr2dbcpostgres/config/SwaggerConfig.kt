package com.toy.webfluxr2dbcpostgres.config

import com.toy.webfluxr2dbcpostgres.common.Constants
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@Configuration
class SwaggerConfig {

  // Swagger 접근 URI: /webjars/swagger-ui/index.html
  @Bean
  fun openAPI(): OpenAPI {
    return OpenAPI()
      .info(info())
      .components(
        Components().addSecuritySchemes(
          Constants.SWAGGER_SECURITY_SCHEME_KEY,
          SecurityScheme().type(SecurityScheme.Type.APIKEY).`in`(SecurityScheme.In.HEADER).name(HttpHeaders.AUTHORIZATION)
        )
      )
  }

  private fun info() = Info()
    .title("API title")
    .description("API description")
    .version("1.0")
}