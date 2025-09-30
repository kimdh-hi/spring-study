## docker-compose support

#### docker compose support
- spring boot 3.1 도입
- 개발환경 초기 설정 간소화 목적
- 팀원 간 일관성 있는 개발환경

#### 설정
1. `developmentOnly("org.springframework.boot:spring-boot-docker-compose")` 추가
2. `compose.yml` 작성
3. `application.yml` 설정 (custom 용도)

```
test 시 docker compose support 적용

//build.gradle.kts
testAndDevelopmentOnly("org.springframework.boot:spring-boot-docker-compose")

//application.yml
spring.docker.compose.skip.in-tests: true
```

#### reference class
- ConnectionDetails
- MariaDbJdbcDockerComposeConnectionDetails

---

reference document
- https://spring.io/blog/2023/06/21/docker-compose-support-in-spring-boot-3-1
