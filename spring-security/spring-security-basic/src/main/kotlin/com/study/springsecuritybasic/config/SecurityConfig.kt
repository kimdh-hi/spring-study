package com.study.springsecuritybasic.config

import com.study.springsecuritybasic.filter.FakeJwtFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

  @Autowired
  lateinit var jacksonConverter: MappingJackson2HttpMessageConverter

  // bean 으로 올려서 필터에 추가하면 ignoredPatternFilterChain 에도 필터가 적용됨
//  @Bean
//  fun jwtFilter() = FakeJwtFilter(jacksonConverter)

  @Bean
  @Order(0)
  fun ignoredPatternFilterChain(http: HttpSecurity): SecurityFilterChain =
    http.authorizeHttpRequests {
      it
        .requestMatchers("/ignoring").permitAll()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
    }
      .authorizeHttpRequests { it.anyRequest().permitAll() }
      .requestCache { it.disable() }
      .securityContext { it.disable() }
      .sessionManagement { it.disable() }
      .build()

  @Bean
  @Order(1)
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
    http
      .authorizeHttpRequests {
        it.anyRequest().authenticated()
      }
      .addFilterBefore(FakeJwtFilter(jacksonConverter), UsernamePasswordAuthenticationFilter::class.java)
//      .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter::class.java)
      .csrf { it.disable() }
      .httpBasic { it.disable() }
      .logout { it.disable() }
      .cors {  }
      .headers { header ->
        header.frameOptions { it.sameOrigin() }
        header.contentTypeOptions { it.disable() }
      }
      .build()
}