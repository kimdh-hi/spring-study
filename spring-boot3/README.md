# Spring Boot 3.x 마이그레이션 노트

## 요약

| 항목 | 전환 시점 | 한 줄 |
| --- | --- | --- |
| [PathPatternParser](#pathpatternparser) | boot 2.6 | 중간 `**` 기동 실패, trailing slash 404 |
| [정적 리소스 404 처리](#정적-리소스-404-처리) | spring 6.1 | 404 가 예외로 바뀌어 catch-all advice 에 걸려 500 |
| [tomcat multipart / parameter 제한](#tomcat-multipart--parameter-제한) | boot 3.5.0~3.5.1 | part 개수·헤더 크기 400, 파라미터 초과분 조용히 유실 |
| [enum 컬럼 타입](#enum-컬럼-타입) | hibernate 6.3 (boot 3.2.0) | `varchar` -> native `ENUM` |
| [@OneToOne FK unique 제약](#onetoone-fk-unique-제약) | hibernate 6.2 | FK 컬럼에 UNIQUE 자동 생성 |
| [detached entity save 예외](#detached-entity-save-예외) | hibernate 6.6 | id 채워진 entity save 시 optimistic lock 예외 |
| [datetime(6) 정밀도](#datetime6-정밀도) | hibernate 6.0 | 저정밀 dialect 클래스 제거로 `datetime` -> `datetime(6)` |
| [조회 컬럼 순서](#조회-컬럼-순서) | hibernate 6.2 | 물리 컬럼 순서 변경 + result set 을 position 으로 read |
| [물리 명명 전략과 envers `_mod` 컬럼](#물리-명명-전략과-envers-_mod-컬럼) | boot 3.0 | 전략 교체는 무해, 전략 **미주입** 경로가 스키마를 가른다 |
| [API 치환](#api-치환) | boot 3.0 | security / integration deprecated API |

각 항목은 **변경 / 영향 / 대응 / 재현 / 참조** 순서로 정리했다.

---

## 웹 / MVC

### PathPatternParser

**변경**

기본값 전환 시점은 **boot 2.6** 이다 (3.0 아님)
- `spring.mvc.pathmatch.matching-strategy`: `ant-path-matcher` -> `path-pattern-parser`
- 2.7 -> 3.x 경로면 이미 적용된 상태라 신규 이슈가 아니다. 2.5 이하에서 올라올 때 이 시점에 처음 만난다

**영향**

AntPathMatcher 와의 차이
- `**` 는 패턴 **마지막 세그먼트에만** 허용. 중간에 두면 기동 시점에 터진다
  - `PatternParseException: No more pattern data allowed after {*...} or ** pattern element`
- suffix pattern matching(`/x.*`) 제거
- `*`, `?` 는 `/` 를 넘지 않음
- trailing slash 매칭은 spring 6 에서 기본 false -> `/some/greeting/` 이 404 (boot 3.0 가이드 명시 항목)

**대응**

- 중간 `**` 는 다중 세그먼트 캡처로 대체

```kotlin
// PatternParseException
@GetMapping("/files/**/download")

// ok
@GetMapping("/files/{*path}")
```

- trailing slash 복원이 필요하면 `WebMvcConfigurer.setUseTrailingSlashMatch(true)` (deprecated, 임시)
- `matching-strategy: ant-path-matcher` 로 되돌릴 수 있으나 actuator 등 기본 경로와 충돌 가능하므로 회피용

**참조**

- boot 3.0 migration guide (trailing slash): https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide
- matching-strategy 기본값 전환(2.6): https://github.com/spring-projects/spring-boot/issues/28936
- PathPatternParser 중간 `**` 거부: https://github.com/spring-projects/spring-framework/issues/24952
- spring security matcher 는 별개 축 — boot4 문서 참고: [../spring-boot4/migration-critical.md](../spring-boot4/migration-critical.md)

---

### 정적 리소스 404 처리

**변경**

404 응답을 만드는 두 경로가 spring 6.1 에서 **예외 throw 로 통일**됐다.
- 이전: 두 곳 모두 `response.sendError(SC_NOT_FOUND)` 직접 호출 -> 예외가 없어 `@ControllerAdvice` 를 안 탄다
- 이후
  - `DispatcherServlet.noHandlerFound` -> `NoHandlerFoundException`
  - `ResourceHttpRequestHandler` -> `NoResourceFoundException` (`ErrorResponseException` 상속, status 404 자체 보유)

`throwExceptionIfNoHandlerFound` 기본값 전환 (spring 6.1)
- 필드 기본값이 `false` -> `true`, setter 는 `@Deprecated(since = "6.1", forRemoval = true)`
- boot 프로퍼티 `spring.mvc.throw-exception-if-no-handler-found` 도 제거됨 (실측: 3.5.16 `WebMvcProperties` 에 필드 없음)

**영향**

- `@RestControllerAdvice` 에 `@ExceptionHandler(Exception::class)` 가 있으면 **`ExceptionHandlerExceptionResolver` 가 `DefaultHandlerExceptionResolver` 보다 먼저** 돌아 404 가 **500** 이 된다
- body 는 `No static resource {path}.`
- 마이그레이션 전에는 매핑 없는 URL 이 조용히 404 였으므로 모니터링에 5xx 로 잡히기 시작한다

`spring.web.resources.add-mappings` 기본 true 라서
- 미매칭 요청은 `/**` 리소스 핸들러로 먼저 흘러간다 -> 실제로 던져지는 건 대부분 `NoResourceFoundException`
- `NoHandlerFoundException` 만 핸들링하면 안 잡힌다

**대응** (택1)

- advice 에 `@ExceptionHandler(NoResourceFoundException::class)` 명시 (권장)
- advice 를 `ResponseEntityExceptionHandler` 상속으로 전환 — `ErrorResponse` 계열을 선언된 status 로 처리
- catch-all 핸들러에서 `ErrorResponse` 인스턴스는 재throw

```kotlin
@ExceptionHandler(NoResourceFoundException::class)
fun handleNoResource(e: NoResourceFoundException) =
  ResponseEntity.status(e.statusCode).body(e.message)
```

**재현**: `NoResourceFound500Test` / `NoResourceFound404Test`
- 같은 `GET /no-such-path` 요청이 advice 구성에 따라 500 / 404 로 갈린다

**참조**

- 404 예외화(spring-framework #29491): https://github.com/spring-projects/spring-framework/issues/29491
- boot 3.2.0 404 동작 문의(closed as invalid): https://github.com/spring-projects/spring-boot/issues/38733
- NoResourceFoundException 배경 정리: [../spring-exception/README.md](../spring-exception/README.md)

---

### tomcat multipart / parameter 제한

**변경**

```
This release upgrades to Tomcat 10.1.42 which has introduced limits for part count and header size in multipart/form-data requests. 
These limits can be customized using server.tomcat.max-part-count and server.tomcat.max-part-header-size respectively.
```

boot 3.5.1 / tomcat 10.1.42 — multipart 설정 추가
- `server.tomcat.max-part-count` (default: 10)
  - file, file 외 part 포함 갯수
- `server.tomcat.max-part-header-size` (default: 512 bytes)
- 배경: multipart DoS (CVE-2025-48988) 대응으로 tomcat 10.1.42 에서 두 제한 도입

boot 3.5.0 — `server.tomcat.max-parameter-count` (default: 10000)
- 요청당 파라미터 총 개수 상한 (파라미터 파싱 DoS 방어)
- 쿼리스트링 + `application/x-www-form-urlencoded` 바디 + `multipart` 필드(업로드 파일 포함) 수 **합산**
- 임베디드 tomcat 은 server.xml 이 없어 코드 기본값 10000 적용 (standalone server.xml 은 10.1.8 부터 1000)

**영향**

- part-count / part-header-size 초과: 400
- max-parameter-count 초과분은 예외 없이 조용히 버려짐 -> `@RequestParam`/바인딩 null·누락 (part-count 와 달리 400 아님)
- 3.5.16 (tomcat 10.1.55) 에서도 soft fail 잔존. tomcat 11(boot 4.0)부터 hard fail(요청 거부)로 변경

**대응**

- 프로퍼티로 상한 조정
- `FailedRequestFilter` 적용시 파라미터 초과를 400 으로 거부 가능

**참조**

- CVE-2025-48988: https://github.com/advisories/GHSA-h3gc-qfqq-6h8f
- multipart 프로퍼티 노출: https://github.com/spring-projects/spring-boot/issues/45881
- max-parameter-count 프로퍼티 추가(3.5.0): https://github.com/spring-projects/spring-boot/pull/43286
- tomcat maxParameterCount 문서: https://tomcat.apache.org/tomcat-10.1-doc/config/http.html
- 10.1.x soft / 11.0.x hard fail (dev list, Mark Thomas): https://www.mail-archive.com/dev@tomcat.apache.org/msg176564.html

---

## JPA / Hibernate

아래 [datetime(6) 정밀도](#datetime6-정밀도) / [조회 컬럼 순서](#조회-컬럼-순서) / [물리 명명 전략과 envers `_mod` 컬럼](#물리-명명-전략과-envers-_mod-컬럼) 세 항목은
hibernate 5.6.15 / 6.6.11 최소 프로젝트에 동일 엔티티를 놓고 DDL·생성 SQL 을 덤프해 대조한 결과 기준이다.
현장에서 원인으로 지목된 항목 중 2개는 실제 원인이 다른 곳에 있었다.

### enum 컬럼 타입

**변경**

- boot 3.2.0(hibernate 6.3)부터 mysql/mariadb 에서 `enum` 필드가 native `ENUM` 컬럼 타입으로 매핑됨 (이전은 `varchar`)

**영향**

- `ENUM` type 은 enum 값 추가시 스키마 변경이 필요한 등 문제

**대응**

- `@JdbcTypeCode(SqlTypes.VARCHAR)` 또는 `hibernate.type.preferred_enum_jdbc_type=VARCHAR` 로 `varchar(255)` 강제
- 단, `@JdbcTypeCode(SqlTypes.VARCHAR)` 지정시 data type 은 `varchar` 로 정의되지만 enum 에 대한 제약조건이 붙는다
  - 즉, ENUM data type 을 그대로 사용하는 것과 다를게 없다

```
create table tb_user (
    id varchar(255) not null,
    name varchar(255),
    user_type varchar(255) check (user_type in ('USER','ADMIN')),
    primary key (id)
) engine=InnoDB
```

---

### @OneToOne FK unique 제약

**변경**

hibernate **6.2** 부터 optional logical one-to-one 의 FK 컬럼에 UNIQUE 제약을 생성한다.
- 6.2 migration guide: *"Previous versions of Hibernate did not create a UNIQUE constraint on the database for logical one-to-one associations marked as optional."*
- 근거: FK 는 모델링상 unique 여야 한다는 것

실측 (hibernate 6.6, H2, `ddl-auto=create-drop`)

```
create table tb_one_to_one_owner (
    id bigint generated by default as identity,
    target_id bigint unique,
    primary key (id)
)
```

- `@JoinColumn(unique = ...)` 를 준 적이 없는데 컬럼 정의에 `unique` 가 붙는다
- 5.x 는 동일 매핑에서 FK 만 생성하고 unique 는 없다

**영향**

- `ddl-auto: update` — 기존 테이블에 제약 추가를 시도하고 **중복 row 가 있으면 기동 실패**
- `ddl-auto: validate` — unique 는 검증 대상이 아니라 통과. 로컬/테스트만 `create` 로 돌리면 아예 안 보이다가 스키마 반영 시점에 터진다
- `@OneToOne` 으로 매핑해뒀지만 실제로는 1:N 인 테이블(이력 row 등)이 이 시점에 드러난다

**대응**

- 실제 1:1 이면 수용 — 중복 row 선정리 후 제약 반영 (권장)
- 1:N 이 정상이면 `@ManyToOne` 으로 remap (6.2 guide 권고안)
- 스키마를 못 건드리면 DDL 을 직접 관리(flyway 등)하고 `ddl-auto: validate` 로 고정
  - `@JoinColumn(unique = false)` 로는 못 끈다

**재현**: `OneToOneUniqueConstraintTest`
- 같은 대상을 참조하는 `@OneToOne` 2건 저장 -> `ConstraintViolationException`
- id 전략이 `IDENTITY` 면 `flush()` 가 아니라 **`persist()` 시점**에 즉시 insert 되어 거기서 터진다

**참조**

- hibernate 6.2 migration guide (one-to-one unique): https://docs.hibernate.org/orm/6.2/migration-guide/migration-guide.html

---

### detached entity save 예외

**변경**

- hibernate 5까지 detached entity `merge()` 시 매칭 row 가 없으면 조용히 `INSERT` 수행
- 6.6부터 "확실히 detached 인데 row 없음" 감지시 `StaleObjectStateException` (→ spring `ObjectOptimisticLockingFailureException`) 예외
  - detached entity save 시 insert 발생은 데이터 무결성 위배 가능하므로 예외로 처리
- detached 판정 조건 (둘 중 하나)
  - `@GeneratedValue` `@Id` 가 이미 채워짐
  - non-primitive `@Version` 이 이미 채워짐 (primitive `int/long` 은 0 이 정상 첫 버전이라 unset 표현 불가 → wrapper 타입 필요)

**영향**

- spring 경로: `SimpleJpaRepository.save()` 는 `isNew()` false 시 `merge()` 호출 → id/version 채워진 detached entity 는 merge 로 가고 6.6 에선 row 없으면 예외

**대응**

- new entity 의 id/version 은 기본값(`""`, `0L`) 아닌 `null` 로 둘 것 (가장 흔한 원인)
- `@GeneratedValue` id 수동 할당 금지
- `Persistable<ID>` 구현 / `isNew()` 오버라이드로 persist 강제
- 삭제 후 재삽입은 새 transient 인스턴스로

**재현**: `IdAssignedEntitySaveErrorTest`
- `@Id` 수동 할당 후 save() → `OptimisticLockingFailureException`
- 삭제된 detached entity save() → `OptimisticLockingFailureException`

**참조**

- 6.6 migration guide: https://docs.jboss.org/hibernate/orm/6.6/migration-guide/migration-guide.html#merge-versioned-deleted
- HHH-18527 (Rejected, 의도된 동작): https://hibernate.atlassian.net/browse/HHH-18527
- HHH-17634 (Fixed 6.4.3/6.2.22, merge 가 원본 transient 에 generated id 쓰던 회귀): https://hibernate.atlassian.net/browse/HHH-17634
- spring-boot #37126: https://github.com/spring-projects/spring-boot/issues/37126
- spring-data-jpa #1862: https://github.com/spring-projects/spring-data-jpa/issues/1862
- discourse: https://discourse.hibernate.org/t/facing-with-objectoptimisticlocking-failureexception-after-migrating-to-hibernate-6-6-2-final/10725

---

### datetime(6) 정밀도

**변경**

"hibernate 6 이 정밀도를 올렸다" 가 아니라 **저정밀 dialect 클래스가 제거된 것**이 원인
- 실측 (hibernate 5.6, 동일 엔티티 `LocalDateTime`)

```
MySQL5InnoDBDialect  -> created_date datetime
MySQL55Dialect       -> created_date datetime
MySQL57Dialect       -> created_date datetime(6)
MySQL8Dialect        -> created_date datetime(6)
```

- hibernate 6 은 버전별 MySQL dialect 클래스를 전부 제거하고 단일 `MySQLDialect` + `hibernate.dialect.version` 으로 통합
  - 실측: 6.6 에 `org.hibernate.dialect.MySQL5InnoDBDialect` 클래스 자체가 없음
  - 최소 지원 버전이 5.7 이라 `datetime(6)` 을 피할 수 없다
- 즉 boot 2 에서 `spring.jpa.database-platform: org.hibernate.dialect.MySQL5InnoDBDialect` 를 박아두었던 프로젝트가 이 시점에 `datetime` -> `datetime(6)` 을 맞는다

**영향**

- `ddl-auto: validate` 기동 실패 / `update` 는 컬럼 타입 변경 시도
- 스키마가 `datetime`(정밀도 0) 인데 microsecond 를 보내면 MySQL 이 **반올림**한다 (truncate 아님)
  - 최대 +0.5s 미래 시각 저장. `created_at <= now()` 조건에서 방금 쓴 row 가 안 잡힌다
- timestamp `@Version` optimistic lock 실패 — 저장한 값과 읽은 값의 정밀도가 달라 버전 비교가 깨진다
- `where updated_at = ?` 동등 비교 miss

**대응**

- 스키마를 `datetime(6)` 으로 통일 (권장)
- 낮춰야 하면 필드에 명시

```kotlin
@FractionalSeconds(3)
var updatedAt: LocalDateTime? = null       // datetime(3)

@Column(columnDefinition = "datetime(0)")
var deletedAt: LocalDateTime? = null       // datetime(0)
```

- `@FractionalSeconds` 는 ORM 6.5+ (boot 3.3~)
- `@Column(precision = 3)` 은 **무시된다**. 실측 6.6 / MySQL 에서 `datetime(6)` 그대로 생성

**참조**

- timestamp precision 관련 hibernate 팀 답변: https://discourse.hibernate.org/t/timestamp-value-precision-change/8264

---

### 조회 컬럼 순서

**변경**

독립적인 두 변화가 겹친 결과다.

1. hibernate 6.2 `ColumnOrderingStrategy` 도입, 기본값이 `ColumnOrderingStrategyStandard`
- 정렬 키: `max(physicalSizeBytes, 4)` -> `physicalSizeBytes > 2048` -> name
- 5.x 는 사실상 `id` + 알파벳 순
- 실측 (동일 엔티티, ddl-auto=create)

```
5.6  create table tb_foo (id, amount_value, created_date, first_name, long_text, short_code, updated_date)
6.6  create table tb_foo (amount_value, created_date, id, updated_date, short_code, long_text, first_name)
```

- 신규 생성 테이블의 **물리 컬럼 순서**만 바뀐다. 기존 운영 테이블은 그대로

2. hibernate 6 은 result set 을 alias 가 아닌 **position** 으로 읽는다
- 5.x 는 컬럼별 alias(`foo0_.id as id1_1_`)를 만들고 이름으로 매핑, 6.x 는 alias 없음

```
5.6  select foo0_.id as id1_1_, foo0_.amount_value as amount_v2_1_, ... from tb_foo foo0_
6.6  select f1_0.id,f1_0.amount_value,f1_0.created_date, ... from tb_foo f1_0
```

**영향**

- native `select *` -> `Object[]` 위치 접근. 물리 컬럼 순서가 그대로 배열 인덱스가 되므로 1번과 겹치면 값이 어긋난다
- positional `SqlResultSetMapping`, alias 이름에 의존하는 코드
- 복합 unique index / PK 컬럼 순서 (ddl-auto 로 생성한 경우)

무영향
- 컬럼명으로 읽는 spring data 인터페이스 프로젝션
- 컬럼을 명시 나열한 쿼리

덤으로 native query scalar 타입도 바뀐다
- `select *` 결과 `bigint` 컬럼이 5.6 `BigInteger` -> 6.6 `Long` (H2 실측). 기존 캐스팅 코드가 `ClassCastException`

**대응**

```yaml
spring:
  jpa:
    properties:
      "[hibernate.column_ordering_strategy]": legacy
```

- 실측으로 `legacy` 지정시 5.6 과 동일한 순서로 복귀 확인
- 근본 대응은 native query 의 `select *` 제거와 컬럼 명시

---

### 물리 명명 전략과 envers `_mod` 컬럼

**변경**

`SpringPhysicalNamingStrategy` -> `CamelCaseToUnderscoresNamingStrategy` 교체는 **동작 변화 없음**
- hibernate 쪽 클래스가 boot 구현을 그대로 복사한 것 (소스 주석 `Originally copied from Spring Boot (original name is SpringPhysicalNamingStrategy)`)
- `apply()`, `isUnderscoreRequired()`, `isCaseInsensitive()=true` 전부 동일
- 실측: 5.6 / 6.6 에 같은 전략을 걸면 envers 감사 테이블까지 DDL 이 완전히 일치
- boot 2.6 deprecated -> 3.0 제거는 클래스 소유권 이동일 뿐

실제 변수는 전략 교체가 아니라 **전략 주입 여부**
- 전략은 auto-config(`HibernateProperties`) 가 넣어준다
- 수동 `LocalContainerEntityManagerFactoryBean` 은 hibernate 기본값 `PhysicalNamingStrategyStandardImpl`(무변환) 사용
- 마스터 db 만 auto-config, 테넌트 db 는 수동 설정이면 두 스키마가 갈린다

**영향**

`_mod` 컬럼만 티가 나는 이유
- envers 기본 `LegacyModifiedColumnNamingStrategy` 는 mod 컬럼명을 **java property 이름 + suffix** 로 만든다
- 나머지 감사 컬럼은 원본 엔티티의 물리 컬럼명을 재사용 -> 이미 snake case -> 무변화
- 즉 `@Column(name = "created_date")` 로 명시한 필드도 mod 컬럼은 property 이름을 따라 `createdDate_mod` 로 출발한다

전략 적용 (auto-config)
```
create table tb_foo_aud (id bigint not null, rev integer not null, revtype tinyint,
  created_date datetime(6), created_date_mod bit, first_name varchar(255), first_name_mod bit, ...)
```

전략 미적용 (수동 EMF)
```
create table tb_foo_AUD (id bigint not null, REV integer not null, REVTYPE tinyint,
  created_date datetime(6), createdDate_mod bit, firstName varchar(255), firstName_mod bit, ...)
```

- 감사 테이블명·시스템 컬럼도 함께 갈림
  - `tb_foo_aud` / `rev` / `revtype` / `revinfo` vs `tb_foo_AUD` / `REV` / `REVTYPE` / `REVINFO`
- MySQL 은 테이블명 대소문자를 `lower_case_table_names` 에 따라 구분하므로 `tb_foo_aud` / `tb_foo_AUD` 가 별 테이블이 될 수 있다

**대응**

- 수동 EMF 에도 `hibernate.physical_naming_strategy` 를 동일하게 지정 (근본 대응)
- mod 컬럼을 물리 컬럼명 기준으로 고정하면 property 이름과 무관해진다

```yaml
spring:
  jpa:
    properties:
      "[org.hibernate.envers.modified_column_naming_strategy]": improved
```

- `improved` 실측 결과: 전략 유무와 무관하게 `created_date` -> `created_date_mod`
  - 단 `@Column` 미지정 필드는 물리 컬럼명 자체가 갈리므로(`firstName` vs `first_name`) 전략 통일이 여전히 필요

**참조**

- CamelCaseToUnderscoresNamingStrategy 소스: https://github.com/hibernate/hibernate-orm/blob/6.6/hibernate-core/src/main/java/org/hibernate/boot/model/naming/CamelCaseToUnderscoresNamingStrategy.java
- envers modified column naming: https://github.com/hibernate/hibernate-orm/blob/main/documentation/src/main/asciidoc/userguide/chapters/envers/Envers.adoc
- hibernate 6.0 migration guide: https://docs.hibernate.org/orm/6.0/migration-guide/migration-guide.html

---

## API 치환

### spring security

`EnableGlobalMethodSecurity` deprecated
- Deprecated Use EnableMethodSecurity instead
- `@EnableGlobalMethodSecurity(prePostEnabled = true)` -> `@EnableMethodSecurity(prePostEnabled = true)`

### spring integration

`IntegrationFlows` deprecated

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

### webclient

auto-configuration 으로 타임아웃 설정

```yaml
spring:
  http:
    reactiveclient:
      connect-timeout: 3s
      read-timeout: 30s
```

- https://docs.spring.io/spring-boot/reference/io/rest-client.html#io.rest-client.webclient.configuration
