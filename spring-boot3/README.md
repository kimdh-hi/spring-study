## Springboot 3.x 

### spring security

EnableGlobalMethodSecurity deprecated
- Deprecated Use EnableMethodSecurity instead
- `@EnableGlobalMethodSecurity(prePostEnabled = true)` -> `@EnableMethodSecurity(prePostEnabled = true)`

---

### spring integration

IntegrationFlows deprecated
```kotlin
// deprecated
  @Bean
  fun flow(): IntegrationFlow = IntegrationFlows.from(
    Amqp.inboundAdapter(SimpleMessageListenerContainer(...))
  )
    .handle(activator, "activatorFunction")
    .get()

```

```kotlin
  @Bean
  fun flow(): IntegrationFlow = integrationFlow(SimpleMessageListenerContainer(...)) {
    handle(activator, "activatorFunction")
  }
```

---

### jpa, hibernate

Enum data type 이슈 (boot 3.2.0 / hibernate 6.3~)
- boot 3.2.0(hibernate 6.3)부터 mysql/mariadb 에서 `enum` 필드가 native `ENUM` 컬럼 타입으로 매핑됨 (이전은 `varchar`)
  - `ENUM` type 은 enum 값 추가시 스키마 변경이 필요한 등 문제
- `@JdbcTypeCode(SqlTypes.VARCHAR)` 또는 `hibernate.type.preferred_enum_jdbc_type=VARCHAR` 로 `varchar(255)` 강제

Enum data type 이슈 2
- `@JdbcTypeCode(SqlTypes.VARCHAR)` 지정시 data type 은 `varchar` 로 정의되지만 enum 에 대한 제약조건이 붙는다.
- 즉, ENUM data type 을 그대로 사용하는 것과 다를게 없다.
```
create table tb_user (
    id varchar(255) not null,
    name varchar(255),
    user_type varchar(255) check (user_type in ('USER','ADMIN')),
    primary key (id)
) engine=InnoDB
```

hibernate 6.6.x detached entity save 예외
- hibernate 5까지 detached entity `merge()` 시 매칭 row 가 없으면 조용히 `INSERT` 수행
- 6.6부터 "확실히 detached 인데 row 없음" 감지시 `StaleObjectStateException` (→ spring `ObjectOptimisticLockingFailureException`) 예외
  - detached entity save 시 insert 발생은 데이터 무결성 위배 가능하므로 예외로 처리
- detached 판정 조건 (둘 중 하나)
  - `@GeneratedValue` `@Id` 가 이미 채워짐
  - non-primitive `@Version` 이 이미 채워짐 (primitive `int/long` 은 0 이 정상 첫 버전이라 unset 표현 불가 → wrapper 타입 필요)
- spring 경로: `SimpleJpaRepository.save()` 는 `isNew()` false 시 `merge()` 호출 → id/version 채워진 detached entity 는 merge 로 가고 6.6 에선 row 없으면 예외

재현: `IdAssignedEntitySaveErrorTest`
- `@Id` 수동 할당 후 save() → `OptimisticLockingFailureException`
- 삭제된 detached entity save() → `OptimisticLockingFailureException`

회피법
- new entity 의 id/version 은 기본값(`""`, `0L`) 아닌 `null` 로 둘 것 (가장 흔한 원인)
- `@GeneratedValue` id 수동 할당 금지
- `Persistable<ID>` 구현 / `isNew()` 오버라이드로 persist 강제
- 삭제 후 재삽입은 새 transient 인스턴스로

https://docs.jboss.org/hibernate/orm/6.6/migration-guide/migration-guide.html#merge-versioned-deleted

- HHH-18527 (Rejected, 의도된 동작): https://hibernate.atlassian.net/browse/HHH-18527
- HHH-17634 (Fixed 6.4.3/6.2.22, merge 가 원본 transient 에 generated id 쓰던 회귀): https://hibernate.atlassian.net/browse/HHH-17634
- spring-boot #37126: https://github.com/spring-projects/spring-boot/issues/37126
- spring-data-jpa #1862: https://github.com/spring-projects/spring-data-jpa/issues/1862
- discourse: https://discourse.hibernate.org/t/facing-with-objectoptimisticlocking-failureexception-after-migrating-to-hibernate-6-6-2-final/10725

---

### webclient
- https://docs.spring.io/spring-boot/reference/io/rest-client.html#io.rest-client.webclient.configuration
  - webclient auto-configuration
```yaml
spring:
  http:
    reactiveclient:
      connect-timeout: 3s
      read-timeout: 30s
```

---

### springboot 3.5.1 && tomcat 10.1.42
```
This release upgrades to Tomcat 10.1.42 which has introduced limits for part count and header size in multipart/form-data requests. 
These limits can be customized using server.tomcat.max-part-count and server.tomcat.max-part-header-size respectively.
```
tomcat 설정 추가
- server.tomcat.max-part-count (default: 10)
  - file, file 외 part 포함 갯수
- server.tomcat.max-part-header-size (default: 512 bytes)
- 배경: multipart DoS (CVE-2025-48988) 대응으로 tomcat 10.1.42 에서 두 제한 도입
  - https://github.com/advisories/GHSA-h3gc-qfqq-6h8f
  - boot 프로퍼티 노출: https://github.com/spring-projects/spring-boot/issues/45881

server.tomcat.max-parameter-count (default: 10000, boot 3.5.0~)
- 요청당 파라미터 총 개수 상한 (파라미터 파싱 DoS 방어)
- 쿼리스트링 + `application/x-www-form-urlencoded` 바디 + `multipart` 필드(업로드 파일 포함) 수 **합산**
- 임베디드 tomcat 은 server.xml 이 없어 코드 기본값 10000 적용 (standalone server.xml 은 10.1.8 부터 1000)
- 초과분은 예외 없이 조용히 버려짐 → `@RequestParam`/바인딩 null·누락 (part-count 와 달리 400 아님)
  - `FailedRequestFilter` 적용시 400 으로 거부 가능
- 3.5.16 (tomcat 10.1.55) 에서도 soft fail 잔존. tomcat 11(boot 4.0)부터 hard fail(요청 거부)로 변경

- 프로퍼티 추가(3.5.0): https://github.com/spring-projects/spring-boot/pull/43286
- tomcat maxParameterCount 문서: https://tomcat.apache.org/tomcat-10.1-doc/config/http.html
- 10.1.x soft / 11.0.x hard fail (dev list, Mark Thomas): https://www.mail-archive.com/dev@tomcat.apache.org/msg176564.html
