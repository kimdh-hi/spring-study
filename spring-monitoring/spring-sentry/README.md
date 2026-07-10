# spring-sentry

- Sentry 에러 트래킹 직접 테스트용 프로젝트
- 기술 스택은 `../spring-lgtm`과 동일 (Spring Boot 4.1 / Kotlin 2.3 / JDK 25 / Gradle KTS) + `io.sentry:sentry-spring-boot-4`
- Sentry 개념·기능 정리는 [`sentry.md`](./sentry.md) 참고

## 사전 준비

- **DSN 발급** — sentry.io 무료 Developer 플랜에서 프로젝트 생성 후 DSN 복사 (또는 self-hosted DSN)
- **MySQL** — `localhost:3306` 필요 (`../spring-lgtm/compose.yaml`의 mysql 재사용 가능), DB `sentry`는 자동 생성

## 실행

```bash
export SENTRY_DSN='https://<key>@o0.ingest.sentry.io/<project>'
./gradlew bootRun
```

- `SENTRY_DSN` 미설정 시 SDK 비활성(no-op) — 앱은 정상 기동하나 이벤트 전송 안 함

## 테스트 엔드포인트

| 엔드포인트 | 동작 | Sentry 결과 |
|------------|------|-------------|
| `GET /demo/ok` | 정상 응답 | 없음 |
| `GET /demo/error` | 미처리 예외 throw | 스타터가 자동 캡처 → Issue |
| `GET /demo/message` | `Sentry.captureMessage` | 메시지 이벤트 |
| `GET /demo/handled` | try/catch + `Sentry.captureException` | 처리 예외 캡처 |

```bash
curl localhost:8080/demo/error
curl localhost:8080/demo/message
curl localhost:8080/demo/handled
```

- Sentry 대시보드 Issues에서 캡처 확인

## 다음 단계

- `@RestControllerAdvice`로 처리 예외·커스텀 컨텍스트(tag/user) 보강
- `release`/`environment` 태깅 + git 연동(suspect commit)
- 알림 채널(Slack) 연결
