package com.study.jwt.config

import com.study.jwt.auth.JwtAuthenticationFilter
import com.study.jwt.auth.JwtUtil
import com.study.jwt.domain.Authority
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class SecurityConfig(
    private val jwtUtil: JwtUtil
): WebSecurityConfigurerAdapter() {

    val JWT_FILETER_PROCESSING_URL = "/api/**"

    @Bean
    fun jwtAuthenticationFilter() = JwtAuthenticationFilter(jwtUtil, JWT_FILETER_PROCESSING_URL)

    override fun configure(http: HttpSecurity) {
        http
            .csrf { it.disable() }
            .headers { header ->
                header.frameOptions { it.sameOrigin() }
                header.contentTypeOptions { it.disable() }
            }
            .authorizeHttpRequests {
                it.antMatchers("/accounts/signup", "/accounts/login").permitAll()
                it.anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun roleHierarchy(): RoleHierarchy {
        val roleHierarchy = RoleHierarchyImpl()
        roleHierarchy.setHierarchy("${Authority.ADMIN} > ${Authority.USER}")
        return roleHierarchy
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().requestMatchers(
            PathRequest.toStaticResources().atCommonLocations(),
            PathRequest.toH2Console()
        )
    }
}