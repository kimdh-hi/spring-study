package com.toy.springjavamail.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ITemplateResolver
import java.nio.charset.StandardCharsets

@Configuration
class TemplateEngineConfig {

  @Bean
  fun springTemplateEngine(): SpringTemplateEngine {
    val templateEngine = SpringTemplateEngine()
    templateEngine.addTemplateResolver(htmlTemplateResolver())

    return templateEngine
  }

  @Bean
  fun htmlTemplateResolver(): SpringResourceTemplateResolver {
    val templateResolver = SpringResourceTemplateResolver()
    templateResolver.prefix = "classpath:/static/mail_templates/"
    templateResolver.suffix = ".html"
    templateResolver.templateMode = TemplateMode.HTML
    templateResolver.characterEncoding = StandardCharsets.UTF_8.name()

    return templateResolver
  }
}