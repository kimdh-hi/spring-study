package com.study.jwt.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .csrf{ it.disable() }
            .headers { header ->
                header.frameOptions { it.sameOrigin() }
                header.contentTypeOptions { it.disable() }
            }
            .authorizeHttpRequests {
                it.antMatchers("/accounts/signup", "/accounts/login").permitAll()
            }
    }
}