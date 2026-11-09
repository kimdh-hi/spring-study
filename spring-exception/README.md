# spring exception

## NoResourceFoundException
- https://github.com/spring-projects/spring-framework/issues/29491
- 기존 spring mvc 에서 404 에러를 발생시키는 두 곳
  - DispatcherServlet
  - ResourceHttpRequestHandler
- 두 곳 모두 `sendError(SC_NOT_FOUND)` 를 직접 호출하여 에러 응답
  - exception throw 방식이 아니기 때문에 애플리케이션에서 404 에러를 핸들링하기 어려움
- 개선
  - 두 곳 모두 공통적으로 NoHandlerFoundException throw
- spring 6.1.0

```
// DispatcherServlet.java
// throwExceptionIfNoHandlerFound default false -> true
protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (pageNotFoundLogger.isWarnEnabled()) {
        pageNotFoundLogger.warn("No mapping for " + request.getMethod() + " " + getRequestUri(request));
    }
    if (this.throwExceptionIfNoHandlerFound) {
        throw new NoHandlerFoundException(request.getMethod(), getRequestUri(request),
                new ServletServerHttpRequest(request).getHeaders());
    }
    else {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
```

## 필터 예외 공통 처리 (ErrorController)

- `@RestControllerAdvice` 는 `DispatcherServlet` 내부 예외만 처리
  - Filter 는 `DispatcherServlet` 바깥이라 advice 미적용
- 필터 예외 전파 흐름
  - 필터 예외 -> servlet container -> ERROR dispatch(`/error`) -> `DispatcherServlet` -> `ErrorController`
- `ErrorController` 빈 등록시 boot 기본 `BasicErrorController` 대체
- `ErrorAttributes.getError()` 로 원본 예외 조회
  - `ServletException` 으로 감싸진 경우 cause 로 unwrap
- 예외 없이 `response.sendError()` 만 호출한 경우 `jakarta.servlet.error.status_code` 로 응답 결정
- advice 와 ErrorController 가 `ErrorResponses` 를 공유하여 동일한 응답 포맷 유지
- 필터는 OncePerRequestFilter 로 작성하여 ERROR dispatch 시 재실행 방지
- MockMvc 는 ERROR dispatch 미수행
  - 필터 예외 테스트는 `RANDOM_PORT` + `RestTestClient` 로 실제 서버 사용

### Reference

- https://docs.spring.io/spring-boot/reference/web/servlet.html#web.servlet.spring-mvc.error-handling
