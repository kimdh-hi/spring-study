분산환경 로그 추적 (zipkin, sleuth)

- 클라이언트의 요청이 두 개 이상의 서비스를 거치는 MSA 환경에서 해당 클라이언트의 요청로그를 각 서비스에서 추적하는 것은 쉽지않다.
- sleuth 는 클라이언트 요청마다 고유한 Id (trace_id) 를 부여해서 각 서비스에서 클라이언트를 식별할 수 있도록 한다.
- zipkin 은 trace_id 를 기반으로 분산환경에서 로그를 추적하고 UI 상에서 확인할 수 있도록 하는 라이브러리이다.

```
sleuth 에서 로그추적 정보를 zipkin 으로 보내 시각화하여 로그를 추적 관리하는 것.
```

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

