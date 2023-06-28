## Spring swagger (openapi3)

접근 <br/>
http://localhost:8080/swagger-ui/index.html

### springboot 3.1.x
기존
```kotlin
//  implementation("org.springdoc:springdoc-openapi-kotlin:1.7.0")
//  implementation("org.springdoc:springdoc-openapi-ui:1.7.0")
```

수정
```kotlin
implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
```