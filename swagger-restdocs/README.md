# Swagger REST Docs 샘플

- Spring Boot 4.1.1, Kotlin 2.4.20, JDK 25, Gradle 9.1.0 기반 User CRUD API.
- `domain/model`, `domain/repository`, `application`, `ui` 패키지 구조.
- `UserRepository`의 `JpaRepository` 직접 상속으로 별도 infra 어댑터 없음.
- Spring REST Docs 테스트 결과를 ePages `restdocs-api-spec`으로 집계한 OpenAPI 3.0.1 JSON.
- 운영 코드의 Swagger 및 OpenAPI 애노테이션과 런타임 스캔 의존성 없음.
- WebJar와 `webjars-locator-lite`로 버전 없는 경로에서 서빙하는 Swagger UI.

## 실행

- IntelliJ `SwaggerRestdocsApplication` 실행 또는 `./gradlew bootRun` 실행.
- `http://localhost:8080/swagger-ui/index.html` 경로의 Swagger UI.
- `http://localhost:8080/docs/openapi3.json` 경로의 OpenAPI JSON.
- `http://localhost:8080/h2-console` 경로의 H2 콘솔.

## 문서 갱신

- API 변경 후 `./gradlew updateApiSpec` 실행.
- `UserControllerTest` 실행 결과로 `build/api-spec/openapi3.json` 생성.
- 생성 파일을 `src/main/resources/static/docs/openapi3.json`로 복사.
- 복사 후 `X-API-KEY` 헤더 방식의 `securitySchemes`와 전역 `security` 주입.
- 복사된 스펙 파일 커밋으로 PR 리뷰에서 API 변경 diff 확인.

## 설계 선택

- 스펙 파일 커밋 방식으로 IDE 실행, `bootRun`, `bootJar`의 동일한 정적 리소스 사용.
- 테스트 결과를 `processResources`에 연결하면 `test -> classes -> processResources` 순환 의존 발생.
- `MockMvcRestDocumentationWrapper.document`로 REST Docs 스니펫과 OpenAPI 리소스 스니펫을 한 번에 생성.
- `@AutoConfigureMockMvc`, `@AutoConfigureRestDocs`로 MockMvc 수동 설정 제거.
- ePages 플러그인의 API Key 스킴 미지원으로 `updateApiSpec` 후처리에서 스킴 주입.
- Swagger UI `persistAuthorization` 옵션으로 새로고침 후 입력 키 유지.
- 상대 경로 server URL(`/`)로 Swagger UI를 서빙한 환경의 origin 호출.

## 제약

- REST Docs 테스트가 없는 엔드포인트의 OpenAPI 문서 누락.
- `updateApiSpec` 미실행 시 커밋된 스펙과 실제 API 불일치 가능.
- Wrapper 방식의 path parameter 타입이 `string`으로 고정되며, 타입 지정에는 `resource(ResourceSnippetParameters)` 사용 필요.
- ePages `0.20.1` 출력 형식의 OpenAPI 3.0.1 제한.
- 서버의 `X-API-KEY` 검증 로직 부재로 헤더 전송만 동작.
- 게이트웨이 접두 경로가 붙는 환경에서는 `setServer` 경로 변경 필요.
- Gradle 10 도입 전 ePages 플러그인 호환성 재검증 필요.
