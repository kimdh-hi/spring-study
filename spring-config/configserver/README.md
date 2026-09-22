# Config Server

## 구성

- 설정 저장소는 `CONFIG_GIT_URI` 환경변수로 지정
- 파일명 규칙: `{application}.yml`, `{application}-{profile}.yml`

## 확인

```bash
curl localhost:8888/foo/default
curl localhost:8888/foo/dev
curl localhost:8888/foo-dev.yml
```

- 엔드포인트 형식: `/{application}/{profile}[/{label}]`
- `label` 은 git branch — 기본값은 `default-label: main`

## 실행

```bash
CONFIG_GIT_URI=https://github.com/{org}/{repo} ./gradlew bootRun
```

```yaml
spring:
  cloud:
    config:
      server:
        git:
          username: ${GIT_USERNAME}
          password: ${GIT_TOKEN}
```

## git fetch 주기

- 기본값 `refresh-rate: 0` — config 조회 요청마다 git fetch
- 클라이언트가 짧은 주기로 폴링하면 그만큼 원격 저장소 호출이 늘어남
- `refresh-rate` 를 초 단위로 올리면 해당 시간 내 요청은 로컬 clone 으로 응답

```yaml
spring:
  cloud:
    config:
      server:
        git:
          refresh-rate: 30
```

- 반영 지연 = 클라이언트 폴링 주기 + `refresh-rate`

## 암호화 (대칭키)

- `encrypt.key` 가 설정되면 `/encrypt`, `/decrypt` 엔드포인트가 활성화됨
- `org.springframework.cloud.bootstrap.encrypt.KeyProperties` 참고
  - 대칭키, 비대칭키 모두 지원 
- 키는 커밋하지 않고 환경변수로만 주입 — 미지정 시 기동 실패

```bash
CONFIG_ENCRYPT_KEY={key} ./gradlew bootRun
```

### 값 암호화

```bash
curl -s --data-urlencode "=my-db-password" localhost:8888/encrypt
```

- 출력된 문자열을 repo 의 `{application}.yml` 에 `{cipher}` prefix 로 저장

```yaml
datasource:
  password: '{cipher}AQBx...'
```

- 따옴표 필수 — YAML 이 `{` 를 flow mapping 으로 해석함

### 복호화 위치

- 기본값: **서버가 복호화**해서 평문을 클라이언트로 전달 — 키는 서버에만 있으면 됨
- `spring.cloud.config.server.encrypt.enabled: false` 로 두면 `{cipher}` 그대로 전달, 클라이언트가 각자 복호화 (모든 클라이언트에 키 배포 필요)
- 복호화 실패 시 해당 키는 `invalid.` prefix 가 붙어 내려감 — 조용히 틀린 값이 나가지 않음

### 주의

- `/encrypt`, `/decrypt` 는 인증 없이 열려 있으면 누구나 복호화 가능 — 인증 적용 또는 운영에서 차단
- 전송 구간은 평문이므로 HTTPS 필수

## 클라이언트 연결

```yaml
spring:
  application:
    name: foo
  config:
    import: "configserver:http://localhost:8888"
```
