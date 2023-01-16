## Spring Cloud


### Eureka discovery server

eureka client 등록시 랜덤포트 (server.port=0) 지정하는 경우<br/>
동일 인스턴스 기동시 eureka server 는 동일한 포트로 인식하고 한 개 서비스만 등록한다.<br/>
이를 방지하기 위해 eureka server 에 등록하는 이름을 직접 지정한다.<br/>
```yml
server.port: 0

eureka:
  instance:
    instance-id: ${spring.cloud.client.hostname}:${spring.application.instance_id:${random.value}}
```

---

### Spring cloud gateway

#### 설정파일 기반 설정
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

`http://localhost:8000/first-service` ==> `http://localhost:8881/first-service` <br/>
`http://localhost:8000/second-service` ==> `http://localhost:8882/second-service` <br/>

#### 설정 클래스(코드) 기반 설정
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

#### Filter

GlobalFilter -> CustomFilter ...

GlobalFilter 설정
```yml
spring:
  cloud:
    gateway:
      default-filters:
        - name: GlobalFilter
          args:
            baseMessage: GlobalFilter message
            preLogger: true
            postLogger: true
      routes:
        - id: first-service
          uri: http://localhost:8881
          predicates:
            - Path=/first-service/**
          filters:
            - AddRequestHeader=first-req-header-key,first-req-header-value
            - CustomFilter            
        - id: second-service
          uri: http://localhost:8882
          predicates:
            - Path=/second-service/**
          filters:
            - AddRequestHeader=second-req-header-key,second-req-header-value
            - name: LoggingFilter
              args:
                baseMessage: LoggingFilter message
                preLogger: true
                postLogger: true            
```

필터 설정시 `args` 는 정의한 Filter 클래스 내에 매핑할 클래스를 위치시킨다.
```kotlin
@Component
class GlobalFilter: AbstractGatewayFilterFactory<GlobalFilter.Config>(Config::class.java) {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun apply(config: Config): GatewayFilter = GatewayFilter { exchange, chain ->
    val request = exchange.request
    val response = exchange.response

    log.info("[Global-filter] baseMessage: {}", config.baseMessage)
    if(config.preLogger) {
      log.info("[Global-filter] start request.id: {}", request.id)
    }

    chain.filter(exchange).then(Mono.fromRunnable {
      if(config.postLogger) {
        log.info("[Global-filter] end response code: {}", response.statusCode)
      }
    })
  }

  data class Config(
    val baseMessage: String,
    val preLogger: Boolean,
    val postLogger: Boolean
  )
}
```

---

### Config server

```yml
spring:
  cloud:
    config:
      name: ecommerce
  config:
    import: optional:configserver:http://localhost:8888

or

# bootstrap.yml 추가 후 application.yml 에 추가
spring:
  config:
    import:
      - classpath:/bootstrap.yml      
```

config server 변경된 프로퍼티 반영
- 서버 restart
- Spring actuator  Refresh
  - `/actuator/refresh`
- Spring cloud bus