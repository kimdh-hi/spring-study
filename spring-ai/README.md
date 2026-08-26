## Spring AI

### MCP server

- `org.springframework.ai:spring-ai-starter-mcp-server-webmvc` 사용.
- 전송 방식 지정: `spring.ai.mcp.server.protocol=STREAMABLE` 기반 Streamable HTTP 사용.
- mcp 엔드포인트
  -  default: `/mcp`
  - `spring.ai.mcp.server.streamable-http.mcp-endpoint` 통해 커스텀
- Spring Bean 메서드에 `@McpTool` 적용시 mcp 툴로 등록
  - 도구 매개변수 정의는 `@McpToolParam` 적용.
- 인증
  - https://docs.spring.io/spring-ai/reference/api/mcp/mcp-security.html 
  - MCP HTTP 엔드포인트의 기본 인증 및 인가 미제공. 
    - - Spring AI MCP Security 모듈은 커뮤니티 기반 WIP 상태. (https://github.com/spring-ai-community/mcp-security)
  - 일반 HTTP 요청과 동일하게 Spring Security `SecurityFilterChain` 통해 인증/인가
  - `/mcp` 경로에 적용한 Spring Security 인증 및 인가 정책 사용.

### pgvector vector store, embedding setting

```shell
docker pull pgvector/pgvector:pg18
```

```shell
docker run -d --name pgvector \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD=postgres \
-p 5432:5432 \
-v pgdata:/var/lib/postrgresql/data \
pgvector/pgvector:pg18
```

```kotlin
implementation("org.springframework.ai:spring-ai-starter-vector-store-pgvector")
```

```yaml
spring:
  ai:
    vectorstore:
      pgvector:
        initialize-schema: true
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: postgres
```



---

## reference 
- https://docs.spring.io/spring-ai/reference/index.html
