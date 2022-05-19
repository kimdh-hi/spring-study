package com.study.jwt.config

import com.study.jwt.auth.JwtAuthenticationFilter
import com.study.jwt.auth.JwtUtil
import com.study.jwt.base.SecurityConstants
import com.study.jwt.domain.Authority
import org.apache.catalina.startup.Constants
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
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

    @Bean
    fun jwtAuthenticationFilter() = JwtAuthenticationFilter(jwtUtil, SecurityConstants.JWT_FILTER_PROCESSING_URL_PREFIX)

    override fun configure(http: HttpSecurity) {
        http
            .csrf { it.disable() }
            .headers { header ->
                header.frameOptions { it.sameOrigin() }
                header.contentTypeOptions { it.disable() }
            }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
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