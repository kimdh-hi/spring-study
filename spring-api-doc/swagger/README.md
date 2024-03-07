## Spring swagger (openapi3)

접근 <br/>
http://localhost:8083/swagger-ui/index.html

### springboot 3.1.x
기존 boot 2.7.x 사용시
```kotlin
implementation("org.springdoc:springdoc-openapi-kotlin:1.7.0")
implementation("org.springdoc:springdoc-openapi-ui:1.7.0")
```

수정
```kotlin
implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
```

### enumsAsRef = true 글로벌 설정
https://github.com/springdoc/springdoc-openapi/issues/232 <br/>
```kotlin
  companion object {
    init {
      io.swagger.v3.core.jackson.ModelResolver.enumsAsRef.enumsAsRef = true
    }
  }
```

### 참고
https://springdoc.org/v2/ <br/>
https://github.com/springdoc/springdoc-openapi/issues/232