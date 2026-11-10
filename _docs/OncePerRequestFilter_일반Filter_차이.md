# OncePerRequestFilter, 일반 Filter

## Servlet Container

- tomcat, jetty...
- http 요청을 받아 요청 정보에 따라 FilterChain(filter + servlet) 을 구성하고 실행
- filter, servlet 생성주기와 스레드 관리 등을 담당

## Servlet

- `/` 이하 요청 `DispatcherServlet` 이 받아 처리
- 기본적으로 Servlet 은 `DispatcherServlet` 만 사용됨
    - `ServletRegistrationBean` 통해 특정 경로에 대해 Servlet 빈을 등록 가능하지만 드뭄

## Dispatch

- Servlet Container 가 http 요청을 Servlet 에 전달하는 것
- Servlet Container 는 요청 정보(URL, DispatchType..)를 기반으로 호출 필요한 Filter 를 묶어 FilterChain 구성
    - FilterChain 구성시 한 개 Servlet을 포함 (기본적으로 `DispatcherServlet` 이 포함)
- FilterChain 내 Filter 가 순차적으로 실행하고 FilterChain 은 `DispatcherServlet` 을 호출
- 한 개 http 요청은 서버 내부에서 여러 번 dispatch 될 수 있음
    - DispatchType REQUEST 로 첫 요청시 1회
    - 요청 처리중 예외 발생한 경우 별도로 핸들링하지 않은 경우 servlet container 까지 호출 스택이 올라갔다가 servlet container 가 다시 DispatchType ERROR 로 요청을 넘김
    - springboot 의 경우 `GET /error` 로 ERROR dispatch (DispatchType ERROR)
    - 이 때 Servlet Container 는 FilterChain 을 다시 새로 구성하여 요청을 전달
- 필터 등록시 DispatcherType 지정 가능
    - 일반 Filter 의 경우 기본적으로 DispatcherType.REQUEST 로만 등록

## Spring Security FilterChain

- Container FilterChain 안의 `DelegatingFilterProxy` -> `FilterChainProxy` 안에 Security FilterChain 이 중첩되어 있음 (이중 리스트)
- addFilterBefore()로 추가한 필터는 security filterChain 에만 들어감
- security filterChain 안의 필터는 DelegatingFilterProxy의 DispatcherType을 따름
    - 즉, REQUEST뿐 아니라 ERROR, ASYNC 디스패치에서도 실행됨

## OncePerRequestFilter, 일반 Filter

- 일반 Filter 의 경우 실행횟수를 보장하는 장치가 없음
    - 한 개 HTTP 요청은 서버 내부에서 여러 번 dispatch 될 수 있으므로 Filter 또한 여러 번 호출될 수 있음
    - 단, 일반 Filter 를 등록시 DispatchType 을 REQUEST 로 등록하는 경우 최초 요청시 한 번만 호출됨
- OncePerRequestFilter 는 한 개 요청 내에서 한 번만 동작되는 것을 보장
- jwt 파싱 및 검증을 수행하는 filter 를 @Component 로 선언하여 일반 filter 로 등록함과 동시에 SecurityConfig 에서 addFilterBefore 등으로 중복 등록한 경우
    - security filterChain 은 container 의 filterChain 내에 이중리스트처럼 존재
    - 즉, 위 등록한 필터는 container filterChain, security filterChain 에 모두 존재
    - DispatchType 이 REQUEST 여도 위 경우 두 번 호출됨
    - OncePerRequestFilter 를 사용하는 경우 위 경우 한 번만 호출됨
- OncePerRequestFilter이 container filterChain, security filterChain 둘 다 등록된 경우 기본값은 security filterChain 것이 먼저 호출
    - DelegatingFilterProxy order default -100
- DispatchType 차이
    - 일반 Filter 는 기본적으로 DispatcherType.REQUEST 로 등록 (필터 등록시 커스텀 가능)
    - OncePerRequestFilter 는 기본적으로 모든 타입으로 등록 (Security 필터는 REQUEST, ERROR, ASYNC 로 등록)
- 일반 Filter 를 `addFilterBefore()` 통해서만 Security filterChain 에 넣는 경우
    - security filterChain 은 DelegatingFilterProxy의 dispatchType(REQUEST, ERROR, ASYNC) 을 따라감
    - 일반 Filter 로 작성하는 경우 처리되지 않은 예외로 ERROR dispatch 발생시 중복 실행됨

## Reference

- https://docs.spring.io/spring-boot/reference/web/servlet.html#web.servlet.embedded-container
- https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-servlet.html
- https://docs.spring.io/spring-boot/reference/web/servlet.html#web.servlet.embedded-container.servlets-filters-listeners.beans
- https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-filters-review
- https://docs.spring.io/spring-boot/reference/web/servlet.html#web.servlet.spring-mvc.error-handling