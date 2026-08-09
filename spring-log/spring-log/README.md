## Spring / Spring Boot 로그 관리

### 1. 로깅 퍼사드 구조

퍼사드가 필요한 이유
- 라이브러리는 로깅 API 에만 의존하고, 구현체 선택은 애플리케이션이 결정하는 구조 필요
- 구현체를 직접 참조하면 라이브러리마다 다른 로깅 구현이 물려 출력이 분산됨
- SLF4J 가 사실상 표준 퍼사드
- 나는 Logback 을 쓸 것이고, 로그는 한 번 결정하면 잘 바꾸지 않을텐데 굳이 퍼사드 구조가 왜 필요한지?
  - 로깅 라이브러리는 한 번 결정하면 거의 바꿀 일이 없는 것은 맞음
  - 문제는 내가 직접 작성한 로깅 외 내가 의존하는 수많은 라이브러리도 내부적으로 각자 다른 로깅 api 를 호출
  - 여러 라이브러리가 사용하는 로깅 라이브러리를 브리지 통해 SLF4j 인터페이스로 수렴
  - slf4j 인터페이스로 수렴된 로깅 api 요청을 logback, log4j 등의 구현체 통해 처리

구조
```
애플리케이션 / 라이브러리 코드
        ↓  (API)
      SLF4J API          ← 컴파일 시점에 의존하는 유일한 대상
        ↓  (바인딩)
  logback-classic / log4j-slf4j2-impl / slf4j-jdk14
        ↓
   Logback / Log4j2 / JUL   ← 실제 출력 담당
```

- SLF4J API 는 인터페이스만 제공, 실제 출력은 클래스패스에 있는 바인딩이 결정

commons-logging (JCL)
- JCL: Jakarta Commons Logging (Apache Commons Logging)
- SLF4J 이전 퍼사드, 런타임에 클래스패스를 탐색해 구현체를 결정하는 방식
- 클래스로더가 복잡한 환경(WAS, OSGi)에서 구현체 탐색 실패·오탐 문제 발생
- Spring Framework 는 1.x 부터 JCL API 로 작성되어 교체 어려움
  - API 는 그대로 두고 구현만 자체 제작한 것이 `spring-jcl`
- Spring 5+ 는 `spring-jcl` 을 내장해 JCL API 호출을 SLF4J 로 위임
  - 원본 commons-logging 의존성 추가 불필요

브릿지 / 어댑터
- 서드파티가 JCL, Log4j1, JUL 등 다른 API 를 쓰더라도 하나의 출력으로 수렴시키는 장치

| 브릿지 | 대상 API | 역할 |
|---|---|---|
| `jcl-over-slf4j` | commons-logging | JCL 호출 → SLF4J |
| `log4j-over-slf4j` | Log4j 1.x | Log4j1 호출 → SLF4J |
| `log4j-to-slf4j` | Log4j2 API | Log4j2 API 호출 → SLF4J |
| `jul-to-slf4j` | java.util.logging | JUL 호출 → SLF4J |

- 원본 라이브러리는 exclude 하고 브릿지로 대체하는 것이 원칙
- 브릿지와 원본을 동시에 넣으면 무한 루프(StackOverflow) 발생 가능
  - 예: `log4j-to-slf4j` + `log4j-slf4j2-impl` 동시 존재

Spring Boot
- `spring-boot-starter-logging` = Logback + 브릿지(`jul-to-slf4j`, `log4j-to-slf4j`) 조합, 별도 설정 없이 단일 출력 보장
- Log4j2 로 교체시 starter-logging exclude 후 `spring-boot-starter-log4j2` 추가
- 스타터를 쓰는 한 바인딩 구성은 자동 정리됨, 직접 추가한 의존성이 이를 깨는지 확인 필요

---

### 2. 주요 구현체

Logback
- SLF4J 와 같은 개발자가 만든 구현체, Spring Boot 기본값
- 설정 파일: `logback.xml` / `logback-spring.xml`
- 설정 자동 스캔(`scan="true"`) 지원 — 재시작 없이 설정 변경 반영
- `AsyncAppender` 로 비동기 지원하나 큐 기반이라 Log4j2 대비 처리량 낮음
- 대부분의 서비스에는 충분, 기본값 유지 권장

Log4j2
- LMAX Disruptor 기반 비동기 로거가 핵심 차별점 — lock-free 링버퍼로 처리량 우위
- 설정 파일: `log4j2.xml` / `log4j2-spring.xml` (yaml, json, properties 도 가능)
- 플러그인 구조로 확장성 높음, 설정 문법은 Logback 보다 복잡
- Log4Shell(CVE-2021-44228) 이후 2.17+ 필수
- 초당 로그량이 매우 많은 경우에만 교체 고려, 볼륨 측정 없이 바꾸는 것은 지양

JUL (java.util.logging)
- JDK 내장이라 의존성 0 — 이것이 유일한 장점
- 로그 레벨 체계가 다름 (`SEVERE`/`WARNING`/`INFO`/`CONFIG`/`FINE`/`FINER`/`FINEST`)
- 설정 유연성 부족, 성능 열위, 롤링 정책 빈약
- 실무에서는 직접 쓰지 않고 `jul-to-slf4j` 로 우회시키는 대상

Log4j 1.x
- 2015 EOL, 보안 패치 없음 — 발견 즉시 `log4j-over-slf4j` 로 대체

비교

| 항목 | Logback | Log4j2 | JUL |
|---|---|---|---|
| Spring Boot 기본 | O | starter 교체 | X |
| 비동기 성능 | 보통 | 높음 (Disruptor) | 낮음 |
| 설정 자동 스캔 | O | O | X |
| 설정 복잡도 | 낮음 | 높음 | 낮음(대신 빈약) |
| 구조화 로깅 | 인코더 추가 필요 | 내장 JsonTemplateLayout | X |

레벨 체계
- SLF4J: `TRACE < DEBUG < INFO < WARN < ERROR`
- 설정한 레벨 이상만 출력 — `INFO` 설정시 `DEBUG`/`TRACE` 는 무시
- JUL 사용 라이브러리는 브릿지가 레벨을 매핑 (`FINE`→`DEBUG`, `SEVERE`→`ERROR`)

Log4j2 교체 예시
```kotlin
configurations.all {
  exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}
dependencies {
  implementation("org.springframework.boot:spring-boot-starter-log4j2")
}
```

---

### 3. application.yml 로깅 설정

커버 범위
- 패키지별 출력 수준, 파일 경로, 기본 롤링 정책, 출력 패턴, 그룹 지정
- 이 범위를 넘어서면(다중 appender, 조건 분기, 커스텀 인코더, 필터) 설정 파일 필요

```yaml
logging:
  level:
    root: info
    "[com.study]": debug
    "[org.hibernate.SQL]": debug
    "[org.hibernate.orm.jdbc.bind]": trace
    "[org.springframework.web.filter.CommonsRequestLoggingFilter]": debug

  group:
    db: org.hibernate,com.zaxxer.hikari,org.springframework.jdbc

  pattern:
    console: "%d{HH:mm:ss.SSS} %-5level [%thread] %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [%X{traceId}] %logger - %msg%n"
    dateformat: yyyy-MM-dd'T'HH:mm:ss.SSSXXX

  file:
    name: /var/log/app/application.log

  logback:
    rollingpolicy:
      file-name-pattern: /var/log/app/application-%d{yyyy-MM-dd}.%i.log.gz
      max-file-size: 100MB
      max-history: 14
      total-size-cap: 3GB
      clean-history-on-start: true
```

로거명 대괄호 표기
- `logging.level` 은 `Map<String, String>` 바인딩 — relaxed binding 이 키를 정규화(소문자화, `.`/`_` 통일)함
- 소문자·숫자·`-` 외의 문자가 키에 있으면 `"[...]"` 로 묶어야 원본 그대로 보존됨
- 로거명은 대소문자 구분 — `org.hibernate.SQL` 을 그대로 쓰면 `org.hibernate.sql` 로 정규화되어 매칭 실패
```yaml
logging:
  level:
    "[org.hibernate.SQL]": debug          # O
    org.hibernate.SQL: debug              # X — 소문자화되어 안 먹힘
    com.study: debug                      # 동작하지만 표기 통일 위해 [] 권장
```
- YAML 에서 대괄호는 flow sequence 로 파싱되므로 `"` 로 감싸야 함
- 전부 소문자면 `[]` 없이도 동작하지만, 대문자 로거가 섞였을 때 혼란을 막기 위해 전체 통일 권장
- 같은 규칙이 `logging.group` 의 그룹명·`spring.jpa.properties` 등 다른 Map 프로퍼티에도 적용

주요 항목
- `logging.level.<패키지>` — 패키지/클래스 단위 레벨, 더 구체적인 경로가 우선
- `logging.group` — 여러 패키지를 묶어 한 번에 레벨 조정, `logging.level.db: debug` 형태로 사용
- `logging.file.name` — 지정시 파일 출력 활성화, 미지정시 콘솔만
- `logging.file.path` — 디렉터리만 지정 (파일명은 `spring.log` 고정), `name` 과 동시 사용 불가
- `logging.logback.rollingpolicy.*` — Logback 전용, Log4j2 사용시 무시됨

프로파일별 분리
```yaml
---
spring.config.activate.on-profile: local
logging.level:
  "[com.study]": debug

---
spring.config.activate.on-profile: prod
logging.level:
  root: warn
  "[com.study]": info
```

외부 설정 주입
- `logging.config` 로 설정 파일 경로 지정 가능
```yaml
logging:
  config: file:/config/logback-custom.xml
```
- 이미지 재빌드 없이 ConfigMap/볼륨으로 설정 파일을 교체하는 운영 패턴에 활용

한계
- appender 를 하나 더 추가하거나(에러 전용 파일 등) 필터·마스킹이 필요하면 yml 로 불가
- 그 시점에 `logback-spring.xml` 로 전환

---

### 4. logback.xml / logback-spring.xml

차이

| 항목 | `logback.xml` | `logback-spring.xml` |
|---|---|---|
| 로드 주체 | Logback 자체 | springboot |
| 로드 시점 | Spring 컨텍스트 초기화 이전 | Spring 환경 준비 후 |
| `<springProfile>` | 불가 | 가능 |
| `<springProperty>` | 불가 | 가능 |
| springboot 기본 설정 상속 | X | `<include>` 로 가능 |

- springboot 환경이면 `logback-spring.xml` 권장  (`logback.xml` 을 쓸 이유가 거의 없음)
- 둘 다 있으면 `logback.xml` 이 먼저 로드되어 Spring 기능을 못 쓰는 상태가 됨

기본 골격
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="60 seconds">

  <include resource="org/springframework/boot/logging/logback/defaults.xml"/>

  <springProperty scope="context" name="appName" source="spring.application.name" defaultValue="app"/>
  <property name="LOG_PATH" value="${LOG_PATH:-/var/log/app}"/>

  <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%d{HH:mm:ss.SSS} %-5level [%X{traceId}] %logger{36} - %msg%n</pattern>
      <charset>UTF-8</charset>
    </encoder>
  </appender>

  <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>${LOG_PATH}/${appName}.log</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
      <fileNamePattern>${LOG_PATH}/${appName}-%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
      <maxFileSize>100MB</maxFileSize>
      <maxHistory>14</maxHistory>
      <totalSizeCap>3GB</totalSizeCap>
      <cleanHistoryOnStart>true</cleanHistoryOnStart>
    </rollingPolicy>
    <encoder>
      <pattern>${FILE_LOG_PATTERN}</pattern>
    </encoder>
  </appender>

  <appender name="ASYNC_FILE" class="ch.qos.logback.classic.AsyncAppender">
    <appender-ref ref="FILE"/>
    <queueSize>1024</queueSize>
    <discardingThreshold>0</discardingThreshold>
    <neverBlock>false</neverBlock>
  </appender>

  <springProfile name="local | dev">
    <root level="DEBUG">
      <appender-ref ref="CONSOLE"/>
    </root>
  </springProfile>

  <springProfile name="prod">
    <root level="INFO">
      <appender-ref ref="CONSOLE"/>
      <appender-ref ref="ASYNC_FILE"/>
    </root>
    <logger name="com.study" level="INFO"/>
  </springProfile>

</configuration>
```

주요 요소
- `scan="true" scanPeriod="..."` — 설정 파일 변경을 주기적으로 감지해 재적용, 재시작 없이 레벨 변경 가능
- `<include resource="...defaults.xml"/>` — Spring Boot 기본 패턴 변수(`CONSOLE_LOG_PATTERN`, `FILE_LOG_PATTERN`) 상속
- `<springProperty>` — `application.yml` 값을 XML 변수로 주입
- `<property>` — 정적 변수, `${VAR:-기본값}` 으로 환경변수 폴백 가능
- `<springProfile name="a | b">` — 프로파일 조건부 블록, `!prod` 형태의 부정도 가능
- `<logger additivity="false">` — 상위 root appender 로 중복 전파 차단

`<appender-ref>` 중복 주의
- 특정 로거에 appender 를 붙이면 root 에도 전파되어 두 번 출력됨
- 의도한 것이 아니면 `additivity="false"` 지정

AsyncAppender 옵션
- `queueSize` — 큐 크기, 기본 256 은 대부분 부족
- `discardingThreshold` — 큐가 이 비율만큼 차면 하위 레벨 로그 버림, 기본값은 20%(=큐의 20% 남으면 TRACE/DEBUG/INFO 폐기)
  - `0` 으로 두면 폐기 없음 — 유실 불가 로그에는 필수
- `neverBlock` — `true` 면 큐 포화시 블로킹 대신 폐기 (성능 우선)
- `includeCallerData` — 호출자 정보 포함, 비용이 매우 커서 기본값(false) 유지 권장

패턴 비용
- `%class`, `%method`, `%line`, `%F` 는 매 로그마다 스택트레이스 생성 — 처리량 급감
- 운영 패턴에서 제외, 필요한 정보는 메시지에 명시적으로 포함

---

### 5. 로그 파일 관리 권장방안

파일 로깅이 여전히 필요한 경우
- 감사·정산 로그 등 유실이 허용되지 않는 로그
- 수집 파이프라인 장애 대비 로컬 버퍼
- 온프레미스 / VM / 레거시 배포 환경

컨테이너 환경 우선 원칙
- k8s / ECS 에서는 표준출력만 사용, 수집·보관·로테이션은 플랫폼 책임
- 컨테이너 쓰기 레이어나 emptyDir 을 소모 — 노드 디스크 압박과 파드 eviction 위험
- 파드 재시작·스케일 아웃 환경에서 파일 로그는 사실상 조회 불가
- 파일로 남기더라도 중앙 수집이 전제되어야 의미 있음

롤링 정책 선택

| 정책 | 클래스 | 특징 |
|---|---|---|
| 시간 기반 | `TimeBasedRollingPolicy` | 일자 단위 조회 쉬움, 트래픽 편차시 파일 크기 편차 큼 |
| 크기 기반 | `FixedWindowRollingPolicy` | 파일 크기 일정, 시점 특정 어려움 |
| 복합 | `SizeAndTimeBasedRollingPolicy` | 일자 구분 + 크기 상한 동시 확보 — **권장** |

보관 상한
- `maxHistory`(보관 기간)와 `totalSizeCap`(총 용량) **둘 다** 설정 — 하나만으로는 폭증 상황을 못 막음
- `maxHistory` 만 두면 트래픽 급증시 하루치가 디스크를 다 먹음
- `totalSizeCap` 만 두면 오래된 파일이 남아있어 조회 혼란
- `cleanHistoryOnStart="true"` — 기동시 오래된 파일 정리
- 상한 미설정으로 디스크가 가득 차 애플리케이션이 죽는 것이 가장 흔한 로그 사고

압축
- `fileNamePattern` 확장자를 `.gz` / `.zip` 으로 지정하면 자동 압축
- 디스크 절감 효과 크지만 롤링 시점 CPU 부하 발생
- 트래픽 피크와 롤링 시점이 겹치지 않도록 고려

권장 기본값
```xml
<maxFileSize>100MB</maxFileSize>
<maxHistory>14</maxHistory>
<totalSizeCap>3GB</totalSizeCap>
<cleanHistoryOnStart>true</cleanHistoryOnStart>
```
- 일 평균 로그량 × 보관일 < `totalSizeCap` 이 되도록 실측 후 조정

---

### 참고자료

- [SLF4J 매뉴얼](https://www.slf4j.org/manual.html) — 퍼사드·바인딩 구조
- [SLF4J Bridging legacy APIs](https://www.slf4j.org/legacy.html) — 브릿지 선택과 무한 루프 주의사항
- [Logback 매뉴얼](https://logback.qos.ch/manual/index.html) — 설정 파일·appender·롤링 정책 전체
- [Spring Boot Logging 레퍼런스](https://docs.spring.io/spring-boot/reference/features/logging.html) — 레벨·그룹·파일·구조화 로깅
- [Relaxed Binding](https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties.relaxed-binding) — `"[org.hibernate.SQL]"` 대괄호 표기가 필요한 이유
- [Kubernetes 로깅 아키텍처](https://kubernetes.io/docs/concepts/cluster-administration/logging/) — stdout 기반 수집과 kubelet 로테이션
