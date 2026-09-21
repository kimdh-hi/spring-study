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

#### stdio MCP server

- 선택적 stdio 전송 기능 사용.
- 웹 전송 자동 설정(`McpServerSseWebMvcAutoConfiguration`, `McpServerStreamableHttpWebMvcAutoConfiguration`, `McpServerStatelessWebMvcAutoConfiguration`)에 `@Conditional(McpServerStdioDisabledCondition)` 적용.
  - `spring.ai.mcp.server.stdio=true` 설정시 웹 전송 전체 비활성.
  - 단일 프로세스의 stdio와 HTTP 동시 노출 불가.
- 전송 방식만 다르므로 `@McpTool` 재사용 가능
- 프로필 분리 통한 단일 JAR 운용
  - 한계가 있음. stdio 가 요구사항인 경우 stdio mcp 전용으로 프로젝트 자체를 분리하는 것을 먼저 검토 

##### 프로필 설정

- `src/main/resources/application-stdio.yml` 참조.
- `spring.main.web-application-type=none`으로 서블릿 컨테이너 미기동.
- `spring.main.banner-mode=off`, `logging.console.enabled=false`로 표준 출력 오염 차단.
  - 표준 출력은 MCP JSON-RPC 전용이므로 `println`과 콘솔 로그 사용 금지.
  - 진단 로그는 `logging.file.name` 통해 파일로 출력.
- `spring.autoconfigure.exclude`로 데이터소스와 pgvector 자동 설정 제외.
  - 클라이언트가 매 실행마다 프로세스를 기동하므로 기동 비용과 외부 의존 최소화.

##### 샘플 도구

- `StdioWorkspaceMcpTool`에 `@Profile("stdio")` 적용.
  - 로컬 파일 접근 도구는 HTTP 노출 대상에서 제외.
- `list_workspace`, `read_workspace_file` 툴과 `workspace://root` 리소스 제공.
- `sample.stdio.root` 기준 경로 밖 접근 차단.
- `@McpResource`는 `spring.ai.mcp.server.capabilities.resource` 기본값 `true` 기반 동작.

##### 실행

```json
{
  "mcpServers": {
    "spring-ai-stdio": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/spring-ai/build/libs/spring-ai-0.0.1-SNAPSHOT.jar",
        "--spring.profiles.active=stdio"
      ]
    }
  }
}
```

- stdio 전용: `org.springframework.ai:spring-ai-starter-mcp-server` 사용.

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
