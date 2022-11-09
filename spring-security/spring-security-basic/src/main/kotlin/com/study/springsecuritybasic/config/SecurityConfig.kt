package com.study.springsecuritybasic.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig {

    @Bean
    fun configure(http: HttpSecurity) = http
        .authorizeHttpRequests {
            it.antMatchers("/accounts").authenticated()
                .antMatchers("/balances").authenticated()
                .antMatchers("/loans").authenticated()
                .antMatchers("/cards").authenticated()
                .antMatchers("/notices").permitAll()
                .antMatchers("/contacts").permitAll()
        }
        .formLogin()
        .and()
        .httpBasic()
        .and()
        .build()
}