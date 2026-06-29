# jOOQ Sample Project

Spring Boot와 jOOQ를 사용한 샘플 프로젝트입니다. Team과 User 간의 1:N 관계를 구현합니다.

## 프로젝트 개요

- **Spring Boot**: 4.0.2
- **Kotlin**: 2.2.21
- **jOOQ**: 3.19.16
- **Database**: MySQL
- **Java**: 24

## 데이터베이스 스키마

### Team 테이블
```sql
CREATE TABLE team (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### User 테이블
```sql
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_id BIGINT NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_team FOREIGN KEY (team_id) REFERENCES team(id)
);
```

## 시작하기

### 1. 데이터베이스 설정

MySQL 서버를 실행하고 다음 정보로 접속할 수 있도록 설정합니다:

```yaml
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jooq_sample?createDatabaseIfNotExist=true
    username: root
    password: password
```

**참고**: `application.yml`에서 데이터베이스 접속 정보를 본인의 환경에 맞게 수정하세요.

### 2. jOOQ 코드 생성

jOOQ는 데이터베이스 스키마로부터 타입 안전한 Kotlin 코드를 생성합니다.

```bash
./gradlew jooqCodegen
```

이 명령은 다음을 수행합니다:
- `src/main/resources/db/schema.sql` 파일을 읽어 스키마 분석
- `build/generated-sources/jooq` 디렉토리에 Kotlin 코드 생성
- `com.study.jooq.generated` 패키지에 테이블 클래스 생성

**생성된 코드 예시**:
- `com.study.jooq.generated.tables.Team`
- `com.study.jooq.generated.tables.User`
- `com.study.jooq.generated.tables.records.TeamRecord`
- `com.study.jooq.generated.tables.records.UserRecord`

### 3. 애플리케이션 실행

```bash
./gradlew bootRun
```

## jOOQ 설정 상세

### build.gradle.kts 설정

```kotlin
plugins {
  id("org.jooq.jooq-codegen") version "3.19.16"
}

dependencies {
  // jOOQ code generation dependencies
  jooqCodegen("org.jooq:jooq-codegen:3.19.16")
  jooqCodegen("com.mysql:mysql-connector-j")
}

jooq {
  configuration {
    logging = org.jooq.meta.jaxb.Logging.INFO
    generator {
      // Kotlin 코드 생성기 사용
      name = "org.jooq.codegen.KotlinGenerator"

      database {
        // DDL 기반 코드 생성 (실제 DB 연결 불필요)
        name = "org.jooq.meta.extensions.ddl.DDLDatabase"
        properties {
          property {
            key = "scripts"
            value = "src/main/resources/db/schema.sql"
          }
        }
      }

      generate {
        isRecords = true      // Record 클래스 생성
        isPojos = true        // POJO 클래스 생성
        isFluentSetters = true // Fluent setter 메서드 생성
      }

      target {
        packageName = "com.study.jooq.generated"
        directory = "build/generated-sources/jooq"
      }
    }
  }
}
```

### 주요 설정 옵션

- **KotlinGenerator**: Kotlin 코드 생성 (기본은 Java)
- **DDLDatabase**: SQL 스크립트 파일로부터 코드 생성 (실제 DB 연결 불필요)
- **isRecords**: jOOQ Record 클래스 생성 (DB 작업용)
- **isPojos**: POJO 클래스 생성 (데이터 전송용)
- **isFluentSetters**: 메서드 체이닝을 위한 Fluent setter

## API 엔드포인트

### Team API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/teams` | 모든 팀 조회 |
| GET | `/api/teams/{id}` | 특정 팀 조회 |
| GET | `/api/teams/{id}/with-users` | 팀과 소속 유저 함께 조회 |
| GET | `/api/teams/with-users` | 모든 팀과 소속 유저 조회 |
| POST | `/api/teams` | 새 팀 생성 |
| PUT | `/api/teams/{id}` | 팀 정보 수정 |
| DELETE | `/api/teams/{id}` | 팀 삭제 |

### User API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | 모든 유저 조회 |
| GET | `/api/users/{id}` | 특정 유저 조회 |
| GET | `/api/users/team/{teamId}` | 특정 팀의 유저 조회 |
| GET | `/api/users/username/{username}` | username으로 유저 조회 |
| POST | `/api/users` | 새 유저 생성 |
| PUT | `/api/users/{id}` | 유저 정보 수정 |
| DELETE | `/api/users/{id}` | 유저 삭제 |

## 코드 예시

### jOOQ DSLContext 사용 예시

```kotlin
@Repository
class TeamRepository(
    private val dsl: DSLContext
) {
    // 모든 팀 조회
    fun findAll(): List<Team> {
        return dsl.selectFrom(TEAM)
            .fetchInto(Team::class.java)
    }

    // ID로 팀 조회
    fun findById(id: Long): Team? {
        return dsl.selectFrom(TEAM)
            .where(TEAM.ID.eq(id))
            .fetchOneInto(Team::class.java)
    }

    // 팀과 유저를 함께 조회
    fun findByIdWithUsers(id: Long): TeamWithUsers? {
        val team = findById(id) ?: return null

        val users = dsl.selectFrom(USER)
            .where(USER.TEAM_ID.eq(id))
            .fetchInto(User::class.java)

        return TeamWithUsers(team, users)
    }

    // 새 팀 저장
    fun save(team: Team): Team {
        val record = dsl.newRecord(TEAM).apply {
            name = team.name
            description = team.description
        }

        record.store()

        return record.into(Team::class.java)
    }
}
```

### API 호출 예시

#### 팀 생성
```bash
curl -X POST http://localhost:8080/api/teams \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Development Team",
    "description": "Backend development team"
  }'
```

#### 유저 생성
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "teamId": 1,
    "username": "john.doe",
    "email": "john.doe@example.com",
    "fullName": "John Doe"
  }'
```

#### 팀과 유저 함께 조회
```bash
curl http://localhost:8080/api/teams/1/with-users
```

## 프로젝트 구조

```
src/main/kotlin/com/study/jooq/
├── JOoqApplication.kt          # Spring Boot 메인 클래스
├── domain/                      # 도메인 모델
│   ├── Team.kt                 # Team 엔티티
│   ├── User.kt                 # User 엔티티
│   └── TeamWithUsers.kt        # Team + Users DTO
├── repository/                  # 데이터 접근 계층
│   ├── TeamRepository.kt       # Team Repository
│   └── UserRepository.kt       # User Repository
├── service/                     # 비즈니스 로직 계층
│   ├── TeamService.kt          # Team Service
│   └── UserService.kt          # User Service
└── controller/                  # API 계층
    ├── TeamController.kt       # Team API
    └── UserController.kt       # User API

src/main/resources/
├── db/
│   └── schema.sql              # 데이터베이스 스키마
└── application.yml             # 애플리케이션 설정

build/generated-sources/jooq/   # jOOQ 생성 코드 (Git 무시)
```

## jOOQ의 주요 특징

### 1. 타입 안전성 (Type Safety)
- 컴파일 타임에 SQL 오류 감지
- IDE 자동완성 지원
- 리팩토링 안전성

### 2. DSL (Domain Specific Language)
- SQL과 유사한 직관적인 API
- Fluent API로 쿼리 작성

```kotlin
// jOOQ DSL 예시
val users = dsl.select()
    .from(USER)
    .where(USER.TEAM_ID.eq(1))
    .and(USER.EMAIL.like("%@example.com"))
    .orderBy(USER.CREATED_AT.desc())
    .fetch()
```

### 3. 코드 생성 (Code Generation)
- 데이터베이스 스키마로부터 자동 코드 생성
- 스키마 변경 시 재생성으로 동기화 유지

### 4. 다양한 데이터베이스 지원
- MySQL, PostgreSQL, Oracle, SQL Server 등 30+ RDBMS 지원
- SQL Dialect 자동 변환

## 테스트

```bash
# 모든 테스트 실행
./gradlew test

# 특정 테스트만 실행
./gradlew test --tests TeamRepositoryTest
```

## 트러블슈팅

### 1. jOOQ 코드 생성 실패
- `src/main/resources/db/schema.sql` 파일이 존재하는지 확인
- SQL 문법 오류가 없는지 확인
- Gradle 캐시 삭제 후 재시도: `./gradlew clean jooqCodegen`

### 2. 컴파일 오류 (TEAM, USER 클래스를 찾을 수 없음)
- jOOQ 코드 생성을 먼저 실행: `./gradlew jooqCodegen`
- IDE에서 생성된 소스 디렉토리를 소스 루트로 인식시키기

### 3. 데이터베이스 연결 실패
- MySQL 서버가 실행 중인지 확인
- `application.yml`의 접속 정보가 올바른지 확인
- 데이터베이스가 생성되어 있는지 확인 (또는 `createDatabaseIfNotExist=true` 옵션 사용)

### 4. Foreign Key 제약 조건 오류
- User 생성 시 존재하는 Team ID를 사용하는지 확인
- Team 삭제 전 관련 User를 먼저 삭제

## 참고 자료

- [jOOQ 공식 문서](https://www.jooq.org/doc/latest/manual/)
- [jOOQ Code Generation](https://www.jooq.org/doc/latest/manual/code-generation/)
- [jOOQ Kotlin](https://www.jooq.org/doc/latest/manual/code-generation/codegen-kotlin/)
- [Spring Boot jOOQ Starter](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.jooq)

## 라이선스

이 프로젝트는 학습 목적의 샘플 프로젝트입니다.
