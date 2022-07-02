package com.study.springsecuritybasic.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.server.ServerHttpSecurity.http
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
class SecurityConfig: WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
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
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        val userDetailsManager = InMemoryUserDetailsManager()
        userDetailsManager.createUser(
            User.withUsername("admin").password("{noop}1234").authorities("admin").build())
        userDetailsManager.createUser(
            User.withUsername("user").password("{noop}1234").authorities("user").build())

        auth.userDetailsService(userDetailsManager)
    }
}