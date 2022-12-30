
### 자동설정에 의한 디폴트 설정
`SpringBootWebSecurityConfiguration`
```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnDefaultWebSecurity
static class SecurityFilterChainConfiguration {

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin();
        http.httpBasic();
        return http.build();
    }
}
```
직접 설정 클래스를 정의하는 경우 위 설정은 무시된다.

### @AuthenticationPrincipal
`AuthenticationPrincipalArgumentResolver` 에 의해 동작한다.
`AuthenticationPrincipalArgumentResolver` 는 `WebMvcSecurityConfiguration` 에 의해 자동설정 된다.


