# jOOQ 프로젝트 설정 가이드

이 문서는 jOOQ 프로젝트를 처음 설정할 때 알아야 할 모든 내용을 포함합니다.

## 목차

1. [개발 환경 요구사항](#개발-환경-요구사항)
2. [로컬 환경 설정](#로컬-환경-설정)
3. [jOOQ 코드 생성 이해하기](#jooq-코드-생성-이해하기)
4. [개발 워크플로우](#개발-워크플로우)
5. [주요 개념 설명](#주요-개념-설명)
6. [FAQ](#faq)

## 개발 환경 요구사항

- **Java**: 24 이상
- **Kotlin**: 2.2.21
- **Gradle**: 8.x (Wrapper 사용 권장)
- **MySQL**: 8.0 이상
- **IDE**: IntelliJ IDEA 권장

## 로컬 환경 설정

### 1. MySQL 설치 및 설정

#### macOS (Homebrew)
```bash
brew install mysql
brew services start mysql

# 초기 설정
mysql_secure_installation
```

#### Docker 사용
```bash
docker run --name jooq-mysql \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=jooq_sample \
  -p 3306:3306 \
  -d mysql:8.0
```

### 2. 데이터베이스 생성

```sql
CREATE DATABASE jooq_sample CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

또는 `application.yml`에서 `createDatabaseIfNotExist=true` 옵션을 사용하면 자동 생성됩니다.

### 3. 애플리케이션 설정

`src/main/resources/application.yml` 파일에서 데이터베이스 접속 정보를 수정하세요:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jooq_sample?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: your_password  # 본인의 MySQL 비밀번호로 변경
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 4. 프로젝트 빌드

```bash
# 1. jOOQ 코드 생성
./gradlew jooqCodegen

# 2. 프로젝트 빌드
./gradlew build

# 3. 애플리케이션 실행
./gradlew bootRun
```

## jOOQ 코드 생성 이해하기

### 코드 생성 프로세스

```
schema.sql → jOOQ Codegen → Generated Kotlin Code → 컴파일
```

1. **입력**: `src/main/resources/db/schema.sql`
2. **처리**: jOOQ Codegen이 DDL 파싱
3. **출력**: `build/generated-sources/jooq/` 디렉토리에 Kotlin 코드 생성

### 생성되는 코드 구조

```
build/generated-sources/jooq/com/study/jooq/generated/
├── tables/
│   ├── Team.kt                 # Team 테이블 정의
│   ├── User.kt                 # User 테이블 정의
│   └── records/
│       ├── TeamRecord.kt       # Team 레코드
│       └── UserRecord.kt       # User 레코드
└── DefaultCatalog.kt           # 카탈로그 정의
```

### 생성된 코드 사용 예시

```kotlin
import com.study.jooq.generated.tables.references.TEAM
import com.study.jooq.generated.tables.references.USER

// 테이블 참조
val teamTable = TEAM  // Team 테이블 인스턴스
val userTable = USER  // User 테이블 인스턴스

// 컬럼 참조
TEAM.ID           // team.id 컬럼
TEAM.NAME         // team.name 컬럼
USER.TEAM_ID      // user.team_id 컬럼
```

### 코드 생성 설정 옵션

`build.gradle.kts`의 `jooq` 블록에서 설정할 수 있는 주요 옵션:

```kotlin
jooq {
  configuration {
    generator {
      // 코드 생성기 타입 선택
      name = "org.jooq.codegen.KotlinGenerator"  // Kotlin 코드 생성
      // name = "org.jooq.codegen.JavaGenerator"  // Java 코드 생성

      generate {
        isDeprecated = false        // @Deprecated 어노테이션 생성 여부
        isRecords = true            // Record 클래스 생성
        isPojos = true              // POJO 클래스 생성
        isImmutablePojos = false    // 불변 POJO 생성 여부
        isInterfaces = false        // 인터페이스 생성 여부
        isDaos = false              // DAO 클래스 생성 여부
        isFluentSetters = true      // Fluent setter 생성
        isJavaTimeTypes = true      // java.time 타입 사용
      }
    }
  }
}
```

## 개발 워크플로우

### 스키마 변경 시 워크플로우

1. **스키마 수정**
   ```sql
   -- src/main/resources/db/schema.sql
   ALTER TABLE user ADD COLUMN phone VARCHAR(20);
   ```

2. **jOOQ 코드 재생성**
   ```bash
   ./gradlew jooqCodegen
   ```

3. **애플리케이션 코드 수정**
   ```kotlin
   // 새로 생성된 컬럼 사용
   dsl.selectFrom(USER)
       .where(USER.PHONE.eq("010-1234-5678"))
       .fetch()
   ```

4. **컴파일 및 테스트**
   ```bash
   ./gradlew build
   ```

### 새로운 테이블 추가 워크플로우

1. **schema.sql에 테이블 정의 추가**
   ```sql
   CREATE TABLE project (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       team_id BIGINT NOT NULL,
       name VARCHAR(255) NOT NULL,
       FOREIGN KEY (team_id) REFERENCES team(id)
   );
   ```

2. **jOOQ 코드 재생성**
   ```bash
   ./gradlew jooqCodegen
   ```

3. **도메인 모델 생성**
   ```kotlin
   data class Project(
       val id: Long? = null,
       val teamId: Long,
       val name: String
   )
   ```

4. **Repository 생성**
   ```kotlin
   @Repository
   class ProjectRepository(private val dsl: DSLContext) {
       fun findAll() = dsl.selectFrom(PROJECT).fetchInto(Project::class.java)
   }
   ```

## 주요 개념 설명

### 1. DSLContext

`DSLContext`는 jOOQ의 핵심 인터페이스로, SQL 쿼리를 실행하는 진입점입니다.

```kotlin
@Repository
class MyRepository(
    private val dsl: DSLContext  // Spring에서 자동 주입
) {
    fun example() {
        // SELECT * FROM user WHERE id = 1
        val user = dsl.selectFrom(USER)
            .where(USER.ID.eq(1))
            .fetchOne()
    }
}
```

### 2. Record vs POJO

- **Record**: jOOQ의 데이터베이스 레코드 표현 (DB 작업용)
- **POJO**: 순수 데이터 객체 (비즈니스 로직용)

```kotlin
// Record 사용 (DB 작업)
val record = dsl.newRecord(USER).apply {
    username = "john"
    email = "john@example.com"
}
record.store()  // INSERT/UPDATE 자동 판단

// POJO 사용 (데이터 전달)
val user = User(
    username = "john",
    email = "john@example.com"
)
```

### 3. Type-Safe SQL

jOOQ는 컴파일 타임에 SQL 오류를 잡아냅니다.

```kotlin
// ✅ 컴파일 성공 - 올바른 타입
dsl.selectFrom(USER)
    .where(USER.ID.eq(1L))  // BIGINT = Long

// ❌ 컴파일 오류 - 타입 불일치
dsl.selectFrom(USER)
    .where(USER.ID.eq("1"))  // BIGINT ≠ String

// ❌ 컴파일 오류 - 존재하지 않는 컬럼
dsl.selectFrom(USER)
    .where(USER.INVALID_COLUMN.eq(1))  // 컬럼이 존재하지 않음
```

### 4. DDLDatabase vs Meta

jOOQ는 두 가지 방식으로 코드를 생성할 수 있습니다:

#### DDLDatabase (현재 프로젝트 방식)
```kotlin
database {
    name = "org.jooq.meta.extensions.ddl.DDLDatabase"
    properties {
        property {
            key = "scripts"
            value = "src/main/resources/db/schema.sql"
        }
    }
}
```
- **장점**: 실제 DB 연결 불필요, 빠름
- **단점**: 복잡한 스키마는 파싱 실패 가능

#### Meta (실제 DB 사용)
```kotlin
database {
    name = "org.jooq.meta.mysql.MySQLDatabase"
}

jdbc {
    driver = "com.mysql.cj.jdbc.Driver"
    url = "jdbc:mysql://localhost:3306/jooq_sample"
    user = "root"
    password = "password"
}
```
- **장점**: 정확함, 모든 DB 기능 지원
- **단점**: DB 연결 필요, 느림

## FAQ

### Q1. jOOQ 코드가 생성되지 않습니다.

**A:** 다음을 확인하세요:
1. `schema.sql` 파일 경로가 올바른지
2. SQL 문법 오류가 없는지
3. Gradle 캐시 삭제 후 재시도
   ```bash
   ./gradlew clean
   ./gradlew jooqCodegen
   ```

### Q2. IDE에서 생성된 jOOQ 코드를 인식하지 못합니다.

**A:** IntelliJ IDEA 기준:
1. `build/generated-sources/jooq`를 소스 루트로 추가
2. File → Project Structure → Modules → Sources
3. `build/generated-sources/jooq` 디렉토리를 "Sources"로 지정
4. 또는 Gradle Sync: `./gradlew --refresh-dependencies`

### Q3. 스키마 변경이 반영되지 않습니다.

**A:** 코드 생성을 다시 실행하세요:
```bash
./gradlew clean jooqCodegen build
```

### Q4. Record와 POJO 중 무엇을 사용해야 하나요?

**A:**
- **Record**: DB CRUD 작업
- **POJO**: 도메인 모델, API 응답

```kotlin
// Repository: Record 사용
val record = dsl.newRecord(USER)
record.store()

// Service/Controller: POJO 사용
val user = User(username = "john", email = "john@example.com")
```

### Q5. DDLDatabase vs Meta 중 어떤 것을 선택해야 하나요?

**A:**
- **개발 초기/간단한 스키마**: DDLDatabase (빠르고 편리)
- **프로덕션/복잡한 스키마**: Meta (정확하고 안정적)

### Q6. jOOQ vs JPA/Hibernate 어떤 것을 사용해야 하나요?

**A:**
- **jOOQ**: 복잡한 쿼리, SQL 제어 필요, 성능 중시
- **JPA**: CRUD 중심, ORM 편의성, 빠른 개발

### Q7. Kotlin Coroutine을 사용할 수 있나요?

**A:** 네, jOOQ는 Kotlin Coroutine을 지원합니다:
```kotlin
suspend fun findUserAsync(id: Long): User? = withContext(Dispatchers.IO) {
    dsl.selectFrom(USER)
        .where(USER.ID.eq(id))
        .fetchOneInto(User::class.java)
}
```

### Q8. 생성된 코드를 Git에 포함해야 하나요?

**A:** 아니요. `build/` 디렉토리는 `.gitignore`에 포함되어 있습니다.
- CI/CD에서 빌드 시 자동 생성
- 개발자마다 로컬에서 생성

### Q9. 프로덕션 환경에서는 어떻게 설정하나요?

**A:** 환경별 설정 파일 사용:

```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jooq_sample

# application-prod.yml
spring:
  datasource:
    url: jdbc:mysql://prod-server:3306/jooq_prod
  sql:
    init:
      mode: never  # 프로덕션에서는 스키마 자동 생성 비활성화
```

### Q10. 성능 최적화 팁이 있나요?

**A:**
1. **배치 삽입**
   ```kotlin
   dsl.batch(
       records.map { record ->
           dsl.insertInto(USER)
               .set(record)
       }
   ).execute()
   ```

2. **SELECT 컬럼 제한**
   ```kotlin
   // ❌ 모든 컬럼
   dsl.selectFrom(USER).fetch()

   // ✅ 필요한 컬럼만
   dsl.select(USER.ID, USER.USERNAME).from(USER).fetch()
   ```

3. **Connection Pool 설정**
   ```yaml
   spring:
     datasource:
       hikari:
         maximum-pool-size: 10
         minimum-idle: 5
   ```

## 참고 자료

- [jOOQ 공식 문서](https://www.jooq.org/doc/latest/manual/)
- [jOOQ GitHub](https://github.com/jOOQ/jOOQ)
- [Spring Boot + jOOQ](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.jooq)
- [jOOQ Best Practices](https://blog.jooq.org/)
