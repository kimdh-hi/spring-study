분산환경 로그 추적

zipkin docker
```
docker run -d -p 9411:9411 openzipkin/zipkin
```

---

*SpringBoot 3.x 부터 Sleuth 를 지원하지 않는다.*
- `Micrometer Tracing` 프로젝트로 이전한다.
```
Spring Cloud Sleuth will not work with Spring Boot 3.x onward. The last major version of Spring Boot that Sleuth will support is 2.x.

The core of this project got moved to Micrometer Tracing project 

https://docs.spring.io/spring-cloud-sleuth/docs/current-SNAPSHOT/reference/html/
```

```
spring cloud 팀은 분산환경 추적을 위한 sleuth 프로젝트를 만들었지만 이 프로젝트는 spring cloud 에서 분리되는 것이 좋겠다고 판단했다.
이 이유로 Micrometer Tracing 라는 sleuth 의 주요 기능을 포함하는 프로젝트를 만들었다.

https://micrometer.io/docs/tracing
```

---

기본설정

dependencies
```
implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
implementation("io.micrometer:micrometer-tracing-bridge-brave")
implementation("io.zipkin.reporter2:zipkin-reporter-brave")
implementation("io.github.openfeign:feign-micrometer")
```

yml
```
spring:
  application:
    name: serviceA


management:
  tracing:
    sampling:
      probability: 1.0 # default: 0.1
      propagation:
        type: b3
  zipkin:
    tracing:
      endpoint: http://localhost:9411/api/v2/spans


logging:
  pattern:
    level: "[%X{traceId:-},%X{spanId:-}]"
```

zipkin tracing endpoint
- https://zipkin.io/zipkin-api/#/

---

분산환경 로그 추적
- 



