## spring-boot 4.x

최소 요구
- springframework 7.x, jdk 17+, kotlin 2.2+, jakarta ee 11, servlet 6.1, graalvm 25, gradle 8.14+
- 4.1: jOOQ 사용 시 jOOQ 3.20 + jdk 21+

이 프로젝트 실제 버전
- spring boot 4.1.0, jdk 25, kotlin 2.4.10, gradle 9.6.1, spring cloud 2025.1.2


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
- default option 변경으로 인한 런타임 이슈는 [migration-critical.md](./migration-critical.md) 참고

### Programmatic Bean Registration
- https://docs.spring.io/spring-framework/reference/core/beans/java/programmatic-bean-registration.html
- sample: `ProgrammaticBeanRegistrar.kt`
- test: `ProgrammaticBeanRegistrationTest.kt`

---

## migration issue (3.5.x → 4.1.x)

### 런타임에만 드러나는 항목
- 컴파일 타임에 잡히지 않고 운영에서 터지는 항목은 [migration-critical.md](./migration-critical.md) 로 분리
- hibernate 명명 전략 + `globally_quoted_identifiers` (이 프로젝트 노출 상태)
- jackson `WRITE_DATES_AS_TIMESTAMPS`, 역직렬화 기본값 강화
- spring security 인가 규칙 무력화, 리다이렉트 URI 상대 경로
- `PathRequest` 정적 위치에 `/fonts/**` 추가
- `RedisCache` 비동기 쓰기 기본
- hibernate detached 엔티티, 네이티브 쿼리 반환 타입, hql 암시적 select 금지

### 그 외 런타임 동작 변경

#### Hibernate StatelessSession
- 2차 캐시 기본 사용 → 배치 처리에서 stale 데이터·캐시 오염. 해제: `setCacheMode(CacheMode.IGNORE)`
- `hibernate.jdbc.batch_size` 미적용 → 배치 성능 저하. `setJdbcBatchSize()` 또는 `insertMultiple()` 사용

#### Spring Data JPA — 파생 쿼리 전환 + AOT 기본 활성
- 파생 쿼리가 Criteria → JPQL 기반으로 변경 → 생성 SQL 변화. 결과·성능 검증 필요
- AOT 리포지터리 기본 활성. 이상 동작 시 `spring.aot.repositories.enabled=false` 로 격리해 원인 판별

#### PropertyMapper null 처리
- null 값에 adapter/predicate 를 호출하지 않음 → 커스텀 auto-configuration 매핑이 조용히 누락
- null 도 매핑하려면 `always()` 명시

#### Kafka 재시도 토폴로지 (4.1)
- `RetryTopicConfigurationBuilder.sameIntervalTopicReuseStrategy` 기본값 `MULTIPLE_TOPICS` → `SINGLE_TOPIC` → 기존 재시도 토픽과 어긋남
- `spring.kafka.retry.topic.backoff.random` → `...backoff.jitter`

---

## migration 주의 (기동·테스트 전면 실패)

즉시 발견되지만 수정 범위가 큼. 마이그레이션 초반에 처리 권장.

### 테스트 인프라 자동 제공 중단
- `@SpringBootTest` 가 MockMvc/TestRestTemplate/WebClient 미제공 → `@AutoConfigureMockMvc`, `@AutoConfigureTestRestTemplate`, `@AutoConfigureRestTestClient` 추가
- `@MockBean`/`@SpyBean` 제거 → `@MockitoBean`/`@MockitoSpyBean`. **테스트 필드에서만 동작, `@Configuration` 내부 사용 불가** → 단순 치환으로 끝나지 않음
- `TestRestTemplate` 은 `spring-boot-resttestclient`(test) + `org.springframework.boot.resttestclient` import

### Hibernate 도메인 모델 검증 강화
- 3.x 에서 조용히 무시되던 애노테이션 오배치·중복이 오류로 전환 → 기동 실패
  - `@Basic` + `@ManyToOne` 동시 지정, `GenerationType.SEQUENCE` + `@TableGenerator` 조합, `@Id`/`@Version`/`@Embedded` 에 컨버터 적용
- 잘못된 매핑을 방치해온 프로젝트일수록 대량 실패

### Kafka — ZooKeeper 지원 제거
- kafka-clients 4.0, KRaft 전용 → `EmbeddedKafkaBroker` 기반 테스트 재작성
- Spring Retry 의존 제거 → Framework 7 core retry 로 설정 이관

### 기동 전제조건
- 4.1: `spring.jpa.bootstrap-mode=deferred` 는 `AsyncTaskExecutor` 빈 필수. 없으면 기동 실패
- SpEL 기본 연산 상한 10,000 → 복잡한 SpEL 런타임 실패
- Spring Retry 의존성 관리 제거 → 계속 사용 시 버전 명시 필수
- war 배포 시 `server.forward-headers-strategy` 무효 → `ForwardedHeaderFilter` 빈 직접 등록. 누락 시 프록시 뒤 클라이언트 IP·스킴이 틀어짐
- Actuator liveness/readiness probe 기본 활성 → 불필요 시 `management.endpoint.health.probes.enabled=false`

---

## 단순 변경

컴파일러·IDE 가 잡아주는 항목. 일괄 치환 후 빌드로 해소.

- 스타터 rename: `starter-web` → `starter-webmvc`, `starter-aop` → `starter-aspectj`, 슬라이스 테스트 스타터 `starter-<tech>-test`
- 패키지 이동: `BootstrapRegistry`, `EnvironmentPostProcessor`, `PropertyPath`/`TypeInformation`, `orm.hibernate5` → `orm.jpa.hibernate`
- 클래스 rename: `Jackson2ObjectMapperBuilderCustomizer` → `JsonMapperBuilderCustomizer`, `@JsonComponent` → `@JacksonComponent`, `SecurityJackson2Modules` → `SecurityJacksonModules`, `PrePostTemplateDefaults` → `AnnotationTemplateExpressionDefaults`
- 제거된 API: `Session#save/update/delete`, `@Where` → `@SQLRestriction`, `ListenableFuture`, `HttpHeaders` 의 `MultiValueMap` 상속, `javax.*`, Undertow, layertools
- Security DSL: `apply()` → `with()`, `requiresChannel()` → `redirectToHttps()`, `setFilterProcessesUrl` → `setRequiredAuthenticationRequestMatcher`, CSRF 쿠키 setter → `setCookieCustomizer`
- 프로퍼티: `spring.dao.exceptiontranslation.enabled` → `spring.persistence.exceptiontranslation.enabled`, `spring.session.redis` → `spring.session.data.redis`


## 이행 순서

1. 3.5.x 최신 패치로 올려 deprecation 제거
2. Security 는 6.5 로 올려 `PathPatternRequestMatcher` opt-in 검증
3. `spring-boot-properties-migrator` 를 runtime 스코프로 임시 추가해 프로퍼티 변경 진단
4. `spring-boot-starter-classic` 으로 기동 확인 → 개별 스타터로 분해
5. `spring.jackson.use-jackson2-defaults=true` 로 Jackson 격리 후 단계적 해제

---

### reference
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.1-Release-Notes
- https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-7.0-Release-Notes
- https://docs.spring.io/spring-security/reference/migration/index.html
- https://docs.hibernate.org/orm/7.0/migration-guide/migration-guide.html
- https://github.com/spring-projects/spring-data-commons/wiki/Spring-Data-2025.1-Release-Notes
- https://github.com/spring-projects/spring-boot/releases
