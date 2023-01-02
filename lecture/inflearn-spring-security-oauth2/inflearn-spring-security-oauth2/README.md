
## OAuth2 2.0
OAuth = Open + Authorization

OAuth2.0 인가 프레임워크는 애플리케이션이 사용자 대신 사용자의 자원에 대한 제한된 엑세스를 얻기 위해 승인 상호 작용을 함으로 애플리케이션이 자체적으로 엑세스 권하을 얻도록 한다.

OAuth2 이전 방식
- 페이스북의 프로필 정보를 가져오고 싶음
- 우리 애플리케이션은 사용자의 페이스북 인증정보를 입력받고 해당 인증정보로 페이스북에 접근
- 문제
  - id/pw 가 노출
  - 해당 인증정보로 우리 애플리케이션은 사용자의 페이스북 모든 기능을 사용 가능
  - 사용자는 우리 애플리케이션을 100% 신뢰할 수 없음 

#### Keycloak
설치 https://www.keycloak.org/downloads
```
tar -xvzf keycloak-20.0.2.tar.gz
```

keycloak 실행
```
bin/

./kc.sh start-dev
```

---

### 참고

#### 자동설정에 의한 디폴트 설정
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

#### @AuthenticationPrincipal
`AuthenticationPrincipalArgumentResolver` 에 의해 동작한다.
`AuthenticationPrincipalArgumentResolver` 는 `WebMvcSecurityConfiguration` 에 의해 자동설정 된다.


