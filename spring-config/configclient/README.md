# configclient

- `~/config-repo/configclient.yml` 에 `message: hello` 커밋 후 configserver(8888) 기동
- 클라이언트 기동: `CONFIG_PASSWORD={password} ./gradlew bootRun` — configserver 와 같은 값
- 조회: `curl localhost:8080/message`
- 값 변경 후 git commit → `curl -X POST localhost:8080/actuator/refresh` → 재조회 시 갱신

## polling 기반 실시간 갱신

- `ConfigPollingRefresher` 가 주기적으로 `ContextRefresher.refresh()` 호출  (`/actuator/refresh` 와 동일)
- 별도 메시징 브로커가 없는 경우 (rabbitmq, kafka 등 config-bus 기반 사용 불가한 경우) 사용 검토

## 갱신 범위

- `@Value` 는 `@RefreshScope` 가 있는 빈에서만 갱신
- `@ConfigurationProperties` 는 `@RefreshScope` 없이 갱신 가능
  -  단, 생성자 바인딩 `val` 은 반영 안 됨 (`var` 사용)

## @ConfigurationProperties 매핑

- `TestProperties` — prefix `test-property`, 본문 `var` 선언
- `GET /test-property` 로 조회, `@RefreshScope` 없이 갱신
- 값 변경 후 `/actuator/refresh` 또는 폴링 주기마다 반영

```kotlin
@Component
@ConfigurationProperties(prefix = "test-property")
class TestProperties {
  var data1: String = ""
  var data2: String = ""
}
```
