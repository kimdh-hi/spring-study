## Spring Cloud


### Eureka discovery server

eureka client 등록시 랜덤포트 (server.port=0) 지정하는 경우
동일 인스턴스 기동시 eureka server 는 동일한 포트로 인식하고 한 개 서비스만 등록한다.
이를 방지하기 위해 eureka server 에 등록하는 이름을 직접 지정한다.
```yml
server.port: 0

eureka:
  instance:
    instance-id: ${spring.cloud.client.hostname}:${spring.application.instance_id:${random.value}}
```

---

### Spring cloud gateway

설정파일 기반 설정
```yml
server.port: 8000
spring:
  cloud:
    gateway:
      routes:
        - id: first-service
          uri: http://localhost:8881
          predicates:
            - Path=/first-service/**
        - id: second-service
          uri: http://localhost:8882
          predicates:
            - Path=/second-service/**
```

`http://localhost:8000/first-service` ==> `http://localhost:8881/first-service`
`http://localhost:8000/second-service` ==> `http://localhost:8882/second-service`

설정 클래스(코드) 기반 설정
```kotlin
  @Bean
  fun gatewayRouteLocator(builder: RouteLocatorBuilder): RouteLocator = builder.routes()
    .route {
      it.path("/first-service/**")
        .filters { filter ->
          filter
            .addRequestHeader("first-req-header-key", "first-req-header-value")
            .addRequestHeader("first-res-header-key", "first-res-header-value")
        }
        .uri("http://localhost:8881")
    }
    .route {
      it.path("/second-service/**")
        .filters { filter ->
          filter
            .addRequestHeader("second-req-header-key", "second-req-header-value")
            .addRequestHeader("second-res-header-key", "second-res-header-value")
        }
        .uri("http://localhost:8882")
    }
    .build()
```
