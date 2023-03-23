## Spring Security - CORS

CORS 는 아래 요청중 하나라도 다른 경우 발생한다. (SOP: Same-Origin Policy)


- 프로토콜
  - http, https ...
- 도메인
  - my.com, my-my.com ...
- 포트번호

---

### Simple Requests
GET, POST 등으로 서버로 요청시 요청한 `Origin`과 서버가 응답한 `Access-Control-Allow-Origin` 등의 헤더값으로 본인의 출처가 허가되었는지 확인한다.

### Preflight
본 요청에 앞서 `OPTIONS` 요청으로 `Origin` 과 `Header` 등 함께 서버로 요청한다. 이 때 바디는 포함되지 않는다.<br/>

서버는 예비요청의 응답으로 `Access-Control-Allow-Origin` 등을 응답하고 브라우저는 이를 통해 내 출처가 서버로부터 허용됐는지 여부를 확인한다.<br/>

### Preflight 가 보내지는 조건
- 요청 메서드 제한
  - GET, POST, HEAD
- 헤더 제한
  - Accept, Accept-Language, Content-Language, Content-Type...
  - Authorization 등을 포함하는 커스텀 헤더 사용 불가
- Content-Type 제한
  - application/x-www-form-urlencoded
  - multipart/form-data
  - test/plain

위 세 가지 조건 중 하나라도 만족하지 않는 경우 SimpleRequests 가 아닌 Preflight 요청 즉 Option 요청이 선행된다.

---

### SpringBoot, Security CORS 설정

```kotlin
@Configuration
class CorsConfig {

  @Bean
  fun corsConfigurationSource(): CorsConfigurationSource {
    val corsConfiguration = CorsConfiguration().apply {
      addAllowedOriginPattern("*")
      addAllowedMethod("*")
      addAllowedHeader("*")
      allowCredentials = true // 쿠키요청 허용
      maxAge = 1800 // preflight 에 대한 응답을 브라우저가 캐싱하는 시간
    }

    return UrlBasedCorsConfigurationSource().apply {
      registerCorsConfiguration("/**", corsConfiguration)
    }
  }
}

@Configuration
@EnableWebSecurity
class SecurityConfig {

  @Bean
  fun configure(http: HttpSecurity): SecurityFilterChain =
    http
      .cors {  }
      //...
      .build()
}
```

---

### Spring Security(JWT) + CORS 설정시 이슈
jwt 토큰기반 인증을 사용하는 경우 클라이언트가 헤더에 포함하는 토큰 정보를 내가 만든 필터에서 검증하게 된다.<br/>
이 때 `WebMvcConfigurer.addCorsMappings` (mvc방식) 을 사용해서 CORS 를 설정하는 경우 만료된 토큰, 올바르지 않은 토큰에 대해 CORS 에러가 발생할 수 있다.<br/>

`Preflights` 요청은 jwtFilter 를 통과하고 `WebMvcConfigurer.addCorsMappings` 에 의해 설정된 대로  `DefaultCorsProcessor` 에서 preflight에 대한 응답을 구성한다.<br/>
응답에는 `Access-Control-Allow-Origin`을 비롯한 관련 헤더들이 정상적으로 내려간다. <br/>

이제 브라우저는 cors 관련 응답헤더를 보고 cors 여부를 판단한 후 정상적이라면 본 요청을 보낸다.<br/>

본요청은 Jwt 필터에 걸리고 토큰이 만료 혹은 올바르지 않은 경우 그대로 예외를 응답한다. 이 때 `Access-Control-Allow-Origin` 등의 헤더는 응답에 포함되지 않는다.<br/>
때문에 브라우저는 응답을보고 CORS 에러를 발생시킨다.<br/>

cors 핉터가 jwt 필터보다 앞에 위치한다면 위와 같은 문제는 발생하지 않는다. 때문에 spring security 를 사용한다면 spring security filter 보다 앞에서 cors 관련 작업이 수행될 수 있도록 위와 같이 설정해야 한다.

---

### CorsFilter
- 사전요청이든 본요청이든 관계없이 필터를 타게 된다.<br/>
- 실제 필터에서 수행되는 것은 `DefaultCorsProcessor` 이다.<br/>
- `DefaultCorsProcessor` 는 클라이언트로부터 받은 CORS 관련 헤더를 기반으로 허용여부를 검증하고 그에 대한 헤더응답을 구성한다.<br/>
- 응답헤더 구성후 현재 요청이 사전요청 혹은 허용하지 않은 출처라면 다음 필터를 타지 않고 리턴한다.
