# jOOQ 프로젝트 빠른 시작 가이드

## 프로젝트 개요

Spring Boot 4.0.2와 jOOQ 3.19를 사용한 샘플 프로젝트입니다.
Team과 User 간의 1:N 관계를 구현했습니다.

## 기술 스택

- **Spring Boot**: 4.0.2
- **Kotlin**: 2.2.21
- **jOOQ**: 3.19.29 (Spring Boot 관리)
- **Database**: MySQL (프로덕션), H2 (테스트)
- **Java**: 24

## 빠른 시작

### 1. MySQL 준비

```bash
# Docker를 사용하는 경우
docker run --name jooq-mysql \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=jooq_sample \
  -p 3306:3306 \
  -d mysql:8.0
```

### 2. 데이터베이스 설정

`src/main/resources/application.yml`에서 데이터베이스 접속 정보를 수정하세요:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jooq_sample
    username: root
    password: your_password  # 본인의 비밀번호로 변경
```

### 3. 빌드 및 실행

```bash
# 빌드
./gradlew build

# 실행
./gradlew bootRun
```

### 4. API 테스트

```bash
# 팀 생성
curl -X POST http://localhost:8080/api/teams \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Backend Team",
    "description": "Backend development team"
  }'

# 유저 생성
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "teamId": 1,
    "username": "john.doe",
    "email": "john@example.com",
    "fullName": "John Doe"
  }'

# 팀과 유저 함께 조회
curl http://localhost:8080/api/teams/1/with-users
```

## 주요 특징

### 1. jOOQ DSL 사용

코드 생성 없이 jOOQ의 DSL을 사용하여 타입 안전한 쿼리를 작성합니다:

```kotlin
fun findAll(): List<Team> {
    return dsl.select()
        .from("team")
        .fetch { record ->
            Team(
                id = record.get("id", Long::class.java),
                name = record.get("name", String::class.java),
                // ...
            )
        }
}
```

### 2. Repository 패턴

Domain 모델을 사용한 깔끔한 Repository 패턴 구현:

```kotlin
@Repository
class TeamRepository(private val dsl: DSLContext) {
    fun findById(id: Long): Team?
    fun findAllWithUsers(): List<TeamWithUsers>
    fun save(team: Team): Team
    // ...
}
```

### 3. RESTful API

Spring MVC를 사용한 RESTful API 엔드포인트:

- `GET /api/teams` - 모든 팀 조회
- `GET /api/teams/{id}/with-users` - 팀과 유저 함께 조회
- `POST /api/teams` - 팀 생성
- 등등...

## 프로젝트 구조

```
src/main/kotlin/com/study/jooq/
├── JOoqApplication.kt          # Spring Boot 메인 클래스
├── domain/                      # 도메인 모델
│   ├── Team.kt
│   ├── User.kt
│   └── TeamWithUsers.kt
├── repository/                  # 데이터 접근 계층
│   ├── TeamRepository.kt
│   └── UserRepository.kt
├── service/                     # 비즈니스 로직
│   ├── TeamService.kt
│   └── UserService.kt
└── controller/                  # API 계층
    ├── TeamController.kt
    └── UserController.kt

src/main/resources/
├── db/
│   └── schema.sql              # 데이터베이스 스키마
├── application.yml             # 프로덕션 설정
└── (테스트용) application-test.yml
```

## 코드 포맷팅

코드는 Kotlin 표준 스타일 가이드를 따릅니다. IntelliJ IDEA에서:

- **Mac**: `Cmd + Option + L`
- **Windows/Linux**: `Ctrl + Alt + L`

## 문제 해결

### MySQL 연결 실패

- MySQL 서버가 실행 중인지 확인
- `application.yml`의 접속 정보가 올바른지 확인

### 빌드 오류

```bash
# Clean 후 재빌드
./gradlew clean build
```

## 추가 문서

- `README.md` - 전체 프로젝트 문서
- `SETUP_GUIDE.md` - 상세 설정 가이드

## 도움이 필요하신가요?

- [jOOQ 공식 문서](https://www.jooq.org/doc/latest/manual/)
- [Spring Boot 문서](https://docs.spring.io/spring-boot/index.html)
