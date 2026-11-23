# AntPathMatcher, PathPatternParser 관련 이슈

## 개념

### AntPathMatcher

- `spring-core`의 문자열 기반 경로 매칭 구현체 (`org.springframework.util.AntPathMatcher`, spring 초기부터 존재)
- Apache Ant의 경로 패턴 문법(`?`, , `*`)을 따름
- `*`를 위치·횟수 제한 없이 쓸 수 있음 (`/a/**/b`, `/**/x/**` 모두 가능)
- Spring 7부터 웹 요청 매핑 용도는 deprecated (클래스 자체는 유지)

### PathPatternParser

- web 전용 경로 매칭 방식 (`org.springframework.web.util.pattern`, Spring 5.0에서 WebFlux와 함께 도입)
- 패턴 문자열을 기동 시점에 한 번 파싱해 `PathPattern` 객체로 만들어 둠
- 요청 경로도 세그먼트 단위로 파싱(`RequestPath`/`PathContainer`)해서 세그먼트별로 비교
    - 효율적이고, 세그먼트별로 디코딩하므로 인코딩 문제에 안전
- `{*path}` 문법 추가: 여러 단계를 매칭하면서 변수로 캡처
- `*`/`{*path}` 사용 제한: 패턴당 1개, 위치는 끝(6.2.13+부터는 맨 앞도 가능)
- mvc 5.3 부터 선택 가능, 6.0부터 기본값 (Boot는 2.6부터 기본값)

### 인코딩/디코딩 관련 차이

- AntPathMatcher 는 경로를 한 개 문자열로 다룸. 단, 2개 선택지가 있음
    - 경로 전체를 먼저 디코딩 하고 매칭
        - `/files/a%2Fb/info` -> 디코딩 -> `/files/a/b/info`
        - %2F가 진짜 /로 바뀌면서 경로가 3단계에서 4단계로 바뀌므로 매칭 실패
    - 디코딩하지 않고 인코딩 된 채로 비교
- PathPatternParser 은 먼저 `/`로 자르고 조각별로 디코딩
    - `/files/a%2Fb/info`
    - 원본 그대로 `/` 기준으로 자름 (단계 확정)
    - 각 조각을 디코딩
    - 패턴과 비교

## 와일드 카드 규칙

| 형태 | 예 | `AntPathMatcher` | `PathPatternParser` |
| --- | --- | --- | --- |
| `*` (한 단계) | `/*/test` | 가능 | 가능 |
| `**` 맨 끝 한 번 | `/test/**` | 가능 | 가능 |
| `**` 맨 앞 한 번 | `/**/test` | 가능 | 가능 |
| `**` 중간 | `/test1/**/test2` | 가능 | 불가 |
| `**` 두 번 이상 | `/**/test/**` | 가능 | 불가 |
- : `/`를 넘지 못하는 한 단계 대응
- `*`: 0단계 이상 여러 단계 대응
- 맨 앞 ** PathPatternParser: 6.2.13+ / 7.x 가능, 그 이전 불가

## /** 이중 사용시 이슈 (PathPatternParser 인 경우)

- security filter 구성시 `/**/test/**` 처럼 구성시 존재하지 않는 경로로 요청이 들어오는 경우 404가 아닌 500에러 발생
    - `.requestMatchers("/**/test/**").permitAll()`
    - 6.2.12 이하: `PatternParseException: No more pattern data allowed after {*...} or ** pattern element`
    - 6.2.13+/7.x: PatternParseException: Multiple '{*...}' or '**' pattern elements are not allowed"
- 원인1: DispatcherServlet 도달 전 SecurityFilter 수준에서 에러 발생
    - 정상적으로 404 응답되려면 DispatcherServlet 내에서 NoResourceFoundException 가 던져져야 함
    - AuthorizationFilter 수준에서 PatternParseException 발생
- 원인2: 없는 경로에 대해 정적 리소스 핸들러(`/**`) ResourceHttpRequestHandler가 받아서 처리되지 않고 Security 필터가 규칙 검사를 필터 단에서 수행
    - 그 결과 ResourceHttpRequestHandler 는 도달하지 못하고 PatternParseException 예외 발생

## 버전별 비교

| 항목 | Spring 5 (Boot 2) | Spring 6 (Boot 3, 현재) | Spring 7 (Boot 4) |
| --- | --- | --- | --- |
| MVC 기본 해석기 | `AntPathMatcher` | `PathPatternParser` | `PathPatternParser` |
| MVC의 `AntPathMatcher` | 기본값 | 설정으로 선택 가능 | 폐기 예정 상태로 잔존 |
| Security 보안 패턴 해석 | Ant 방식 (`antMatchers`) | 요청을 받을 MVC 매핑의 해석기 위임 | 항상 `PathPatternParser` 방식 |
| 비호환 패턴 발현 시점 | 발현 없음 | 요청 시점, 일부 요청만 500 | 기동 시점, 기동 실패 |

### Spring 5

- springboot 2.6+ MVC 기본값 AntPathMatcher -> PathPatternParser 로 변경
- security 의 antMatchers() 는 mvc 와 무관하게 AntPathMatcher(AntPathRequestMatcher) 가 사용됨

### Spring 6: AntPathMatcher, PathPatternParser 공존

- Spring MVC 요청 매핑 default 는 PathPatternParser
    - AntPathMatcher 로 커스텀 가능 `spring.mvc.pathmatch.matching-strategy=ant-path-matcher`
- Spring Security 는 해석기를 직접 고르지 않고 MVC 에 위임
    - 단, MVC가 없는 경우 AntPathMatcher 가 기본적으로 사용됨

```
  @Override
  protected void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.setUseRegisteredSuffixPatternMatch(true);
    configurer.setUseTrailingSlashMatch(true);
    super.configurePathMatch(configurer);
  }
```

## PathPatternParser `{...}` 내 `|` 포함시 예외

- PathPattern 의 변수 캡처 문법사용시 콜론이 없는 경우 괄호 안 전체를 변수 이름으로 취급
    - 변수 `이름`에는 식별자 규칙이 적용
    - `|` 는 쓸 수 없는 문자
    - 즉, 변수 이름 영역에 `|` 포함시 PatternParseException 예외 발생
- https://github.com/spring-projects/spring-framework/blob/main/spring-web/src/main/java/org/springframework/web/util/pattern/InternalPathPatternParser.java

## Reference

- https://github.com/spring-projects/spring-framework/issues/29643
- https://docs.spring.io/spring-security/reference/servlet/architecture.html
- https://docs.spring.io/spring-boot/reference/web/servlet.html
- https://github.com/spring-projects/spring-framework/issues/35213
- https://github.com/spring-projects/spring-framework/issues/35686
- https://github.com/spring-projects/spring-framework/issues/35686
- https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-servlet/handlermapping-path.html