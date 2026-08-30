# configclient

- `~/config-repo/configclient.yml` 에 `message: hello` 커밋 후 configserver(8888) 기동
- 클라이언트 기동: `CONFIG_PASSWORD={password} ./gradlew bootRun` — configserver 와 같은 값
- 조회: `curl localhost:8080/message`
- 값 변경 후 git commit → `curl -X POST localhost:8080/actuator/refresh` → 재조회 시 갱신
