## spring security Cache-Control header

spring security 사용하는 경우 cache-control header 설정
- Cache-Control: no-cache, no-store, max-age=0, must-revalidate
```
HTTP/1.1 200 
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
X-XSS-Protection: 0
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: SAMEORIGIN
Content-Type: application/json
Content-Length: 2
Date: Wed, 26 Feb 2025 07:11:51 GMT
```

security filter chain cache-control diable
```kt
  @Bean
  fun jwtSecurityConfig(
    http: HttpSecurity,
    jwtManager: JwtManager,
    converter: MappingJackson2HttpMessageConverter
  ): SecurityFilterChain = http
    //...
    .headers { header ->
      header.cacheControl { it.disable() }
    }
    //...
    .build()
```

spring security cache-control no-cache, no-store... 권장
```
In the past Spring Security required you to provide your own cache control for your web application. This seemed reasonable at the time, but browser caches have evolved to include caches for secure connections as well. This means that a user may view an authenticated page, log out, and then a malicious user can use the browser history to view the cached page.
```

특정 api 에 대한 cache-control 설정
```kt
return ResponseEntity
  .ok()
  .cacheControl(CacheControl.maxAge(Duration.ofSeconds(60)))
  .body(response)
```


reference
- https://docs.spring.io/spring-security/site/docs/4.1.x/reference/html/headers.html#headers-cache-control