# Config Server

- Spring Cloud Config Server 샘플 — 원격 git repo 를 설정 저장소로 사용
- Spring Boot 4.0.8 / Spring Cloud 2025.1.3 / Kotlin 2.3.21

## 구성

- `@EnableConfigServer` 하나로 서버 활성화
- 설정 저장소는 `CONFIG_GIT_URI` 환경변수로 지정
- 기본값은 공개 샘플 repo `https://github.com/spring-cloud-samples/config-repo`
- 파일명 규칙: `{application}.yml`, `{application}-{profile}.yml`
- commit 된 내용만 반영

## 실행

```bash
./gradlew bootRun
```

## 확인

```bash
curl localhost:8888/foo/default
curl localhost:8888/foo/dev
curl localhost:8888/foo-dev.yml
```

- 엔드포인트 형식: `/{application}/{profile}[/{label}]`
- `label` 은 git branch — 기본값은 `default-label: main`

## repo 바꾸기

- `CONFIG_GIT_URI` 환경변수로 override, 미지정 시 공개 샘플 repo 사용

```bash
CONFIG_GIT_URI=https://github.com/{org}/{repo} ./gradlew bootRun
```

- 로컬 repo: `CONFIG_GIT_URI=file:///path/to/repo`
- private repo: `username`/`password`(PAT) 또는 SSH URL + `private-key` 추가

```yaml
spring:
  cloud:
    config:
      server:
        git:
          username: ${GIT_USERNAME}
          password: ${GIT_TOKEN}
```

## 클라이언트 연결

```yaml
spring:
  application:
    name: foo
  config:
    import: "configserver:http://localhost:8888"
```
