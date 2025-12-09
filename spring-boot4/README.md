## spring-boot 4.x

- springframework 7.x
- jdk 17+
- kotlin 2.2+
- gradle 9.x


## change

### HTTP Service Clients
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes#http-service-clients
- `@ImportHttpServices`

yml 설정
```yaml
spring:
  http:
    clients:
      connect-timeout: 20s
      read-timeout: 20s
    serviceclient: 
      test: # group name
        base-url: https://jsonplaceholder.typicode.com/todos
        read-timeout: 10s
        apiversion:
          default: 1.0
          insert:
            header: X-Version
```

### API Versioning
https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes#api-versioning
https://docs.spring.io/spring-boot/4.0-SNAPSHOT/reference/web/servlet.html#web.servlet.spring-mvc.api-versioning

### Jackson 3.x support
- https://github.com/FasterXML/jackson/blob/main/jackson3/MIGRATING_TO_JACKSON_3.md
- 패키지명 변경 `com.fasterxml.jackson` --> `tools.jackson`
  - 하위호환 유지 
- default option 변경
  - `DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES` true --> false
  - `SerializationFeature.WRITE_DATES_AS_TIMESTAMPS` true --> false
  - `DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES` false --> true
  - `DeserializationFeature.FAIL_ON_TRAILING_TOKENS` false --> true 
- ObjectMapper --> JsonMapper 권장
  - JsonMapperBuilderCustomizer
- kotlin module
  - `implementation("tools.jackson.module:jackson-module-kotlin")`
---

### reference
- https://github.com/spring-projects/spring-boot/releases
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide
