## springboot4 migration critical issue

3.5.x --> 4.1.x

컴파일 타임에 드러나지 않고 런타임에만 발생하는 항목.
rename, 패키지 이동, 제거된 API 는 컴파일러가 잡아주므로 제외.


### hibernate 명명 전략 + globally_quoted_identifiers
- `@Column` 미지정 필드가 `first_name` 이 아닌 `firstName` 컬럼으로 매핑됨
- 물리적 명명 전략이 `CamelCaseToUnderscoresNamingStrategy` --> `PhysicalNamingStrategySnakeCaseImpl` 로 교체
  - 신규 전략은 `Identifier.isQuoted()` 이면 snake case 변환을 건너뜀
  - `globally_quoted_identifiers=true` 는 모든 식별자를 quoted 로 만들어 전체가 변환 대상에서 제외됨
- `ddl-auto` 설정에 따라 결과가 갈리며, 운영 설정일 때 더 위험
  - `validate`: 기동 실패 (즉시 발견)
  - `update`: 기존 컬럼 옆에 신규 컬럼 생성, 신규 쓰기가 그쪽으로 감
  - `none`: 조회·쓰기 대상 컬럼 불일치
- `auto_quote_keyword` 로 대체 — 예약어와 충돌하는 식별자만 인용하므로 snake case 변환 유지

```yaml
spring:
  jpa:
    properties:
      "[hibernate.auto_quote_keyword]": true
```

- 이 프로젝트 `application.yml` 에 `globally_quoted_identifiers: true` 존재
- 상세: [migration-issue.md](./migration-issue.md)


### jackson WRITE_DATES_AS_TIMESTAMPS 기본값 false
- 응답 날짜 표현이 바뀜. 서버는 200 응답이고 로그에 아무것도 남지 않음
- 날짜 필드를 내보내는 모든 엔드포인트가 대상이라 영향 범위가 가장 넓음

```
3.x: {"createdAt": 1735689600000}
4.x: {"createdAt": "2026-01-01T00:00:00"}
```

```yaml
spring:
  jackson:
    serialization:
      write-dates-as-timestamps: true
```

- 전체 복귀는 `spring.jackson.use-jackson2-defaults: true`
- 날짜 필드 포함 엔드포인트의 응답 JSON 을 고정 문자열로 두고 검증하는 테스트 권장


### jackson 역직렬화 기본값 강화
- `FAIL_ON_NULL_FOR_PRIMITIVES` false --> true: 기존에 통과하던 `{"count": null}` 이 역직렬화 실패
- `FAIL_ON_TRAILING_TOKENS` false --> true: 관대하게 넘어갔던 페이로드가 4xx, 5xx
- 요청 형태에 의존하므로 특정 클라이언트에서만 발생, 부분 장애로 나타남
- primitive 필드를 wrapper 타입으로 변경하거나 `use-jackson2-defaults` 로 복귀
- `FAIL_ON_UNKNOWN_PROPERTIES` true --> false 는 위험하지 않음
  - jackson 자체 기본값 변화일 뿐, springboot 는 3.x 에서도 auto-config 로 false 를 넣고 있었음
- 애노테이션 아티팩트는 `com.fasterxml.jackson` 유지. 패키지 일괄 치환 시 애노테이션까지 바꾸면 깨짐


### spring security 인가 규칙 무력화
- 인증 실패가 아니라 규칙 자체가 매칭되지 않는 형태. 보호 대상 경로가 통과하거나 반대로 전부 차단
- 예외가 없으므로 기존 테스트에 잡히지 않음
- `AntPathRequestMatcher`, `MvcRequestMatcher` 사용 불가. `PathPatternRequestMatcher` 가 기본
- 매칭이 어긋나는 지점
  - 비기본 서블릿 경로에서 `basePath()` 미지정
  - 다중 세그먼트 패턴(`/**/admin/**`) 미지원
  - 컨텍스트 경로를 포함한 상대 URI. 절대 경로만 허용

```kotlin
val servlet = PathPatternRequestMatcher.withDefaults().basePath("/mvc")
http.authorizeHttpRequests { auth ->
  auth.requestMatchers(servlet.matcher("/orders/**")).authenticated()
}
```

- 확장자 매칭은 `regexMatcher("\\.jsp$")` 로 대체
- security 6.5 에서 미리 opt-in 해 검증 후 이관 권장

```kotlin
@Bean
fun requestMatcherBuilder() = PathPatternRequestMatcherBuilderFactoryBean()
```

- 보호 경로별로 미인증 요청이 401, 403 인지 검증하는 테스트 필요
  - "인증되면 200" 만 검증하면 무력화를 잡지 못함
- 이 프로젝트 `SecurityConfig.kt` 는 `anyRequest().permitAll()` + matcher 미사용으로 무영향


### PathRequest 정적 위치에 /fonts/** 추가
- 기존 정적 리소스와 동일한 보안 설정(permitAll)이 `/fonts/**` 에 자동 적용
- 해당 경로를 인증 대상으로 쓰고 있었다면 예고 없이 열림
- `PathRequest.toStaticResources().atCommonLocations()` 사용 지점에서 명시적으로 제외


### RedisCache 비동기 쓰기 기본
- lettuce 사용 시 `cache.put()` 직후 `get()` 이 이전 값을 반환할 수 있음
- 예외도 로그도 없는 간헐 실패. 부하 상황에서만 재현되어 원인 추적이 어려움
- 쓰기 후 즉시 조회, 캐시 갱신 직후 응답 반환 패턴이 위험

```kotlin
RedisCacheWriter.create(connectionFactory, RedisCacheWriter.immediateWrites())
```


### hibernate detached 엔티티
- `CascadeType.SAVE_UPDATE` 제거로 `PERSIST`, `ALL` cascade 경로의 detached 인스턴스는 flush 시점에 `EntityExistsException`
- 3.x 의 "조회한 엔티티를 연관관계에 그대로 set" 패턴이 그대로 터짐. `merge()` 로 재연결 후 설정
- 저장 호출이 아닌 flush 시점 예외라 스택트레이스가 원인 코드를 가리키지 않음
- detached 엔티티 `refresh`, `lock` 은 `IllegalArgumentException`
- `@Id`, `@MapsId` 연관관계의 자동 `PERSIST` cascade 중단


### hibernate 네이티브 쿼리 반환 타입
- 시간 타입이 `java.sql.Timestamp`, `Date` --> `java.time.LocalDateTime`, `LocalDate` 반환
- 기존 캐스팅 코드가 `ClassCastException`

```kotlin
val row = em.createNativeQuery("select id, created_at from users").singleResult as Array<*>
row[1] as Timestamp      // 4.x ClassCastException
row[1] as LocalDateTime  // 4.x
```

- `char`, `Character` DDL 이 `char(1)` --> `varchar(1)`
- 배열 매핑이 `VARBINARY` --> `JSON_ARRAY`, `XML_ARRAY`. 기존 저장 데이터 읽기 불가


### hql 암시적 select 금지
- `createQuery("from X")` 거부. `createQuery("from X", X.class)` 또는 명시 select 필요
- 문자열 쿼리라 컴파일 시점에 잡히지 않음
- 실행 경로를 타야 발견되므로 커버리지 낮은 쿼리는 운영에서 터짐


### spring security 리다이렉트 URI 상대 경로 기본
- `Location: https://host/login` --> `Location: /login`
- 리버스 프록시, 게이트웨이, 경로 재작성 환경에서 로그인 리다이렉트 깨짐
- 로컬은 정상이고 운영에서만 재현됨
- 복원: `LoginUrlAuthenticationEntryPoint.setFavorRelativeUris(false)`


### 점검

```bash
grep -rn "globally_quoted_identifiers" src/main/resources/
grep -rn "AntPathRequestMatcher\|MvcRequestMatcher\|requestMatchers" src/
grep -rn "atCommonLocations" src/
grep -rn "createQuery(" src/
```

---

### reference
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide
- https://docs.hibernate.org/orm/7.0/migration-guide/migration-guide.html
- https://docs.spring.io/spring-security/reference/migration/index.html
- https://github.com/FasterXML/jackson/blob/main/jackson3/MIGRATING_TO_JACKSON_3.md
- https://github.com/spring-projects/spring-data-commons/wiki/Spring-Data-2025.1-Release-Notes
