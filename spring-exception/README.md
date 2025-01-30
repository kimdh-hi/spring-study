## spring exception

NoResourceFoundException
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
