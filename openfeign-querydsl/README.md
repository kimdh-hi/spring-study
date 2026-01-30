## OpenFeign QueryDsl

### why OpenFeign QueryDsl
- querydsl/querydsl 업데이트 거의 안 됨
  - https://github.com/querydsl/querydsl/releases
- CVE-2024–49203 취약점 (Querydsl 5.1.0)
  - orderBy 절에 사용자 입력값을 그대로 사용시 injection 가능

```
CVE-2024–49203

val orderByField = request.getParameter("sort") // 사용자가 "sort=name" 으로 보냄
val path = PathBuilder(User::class.java, "user")
val orderSpecifier = OrderSpecifier(Order.ASC, path.get(orderByField)) // ❌ 취약한 부분
query.orderBy(orderSpecifier)

/users?sort=name INTERSECT SELECT password FROM user

취약 쿼리
- SELECT ... FROM user ORDER BY user.name INTERSECT SELECT password FROM user

버전 업그레이드 외 대응
- 정렬 관련 입력값 ENUM 으로 처리
```
- kotlin kapt maintenance
  - https://kotlinlang.org/docs/kapt.html
  - ksp (Kotlin Symbol Processing API) 대체 권장
```
kapt is in maintenance mode. We are keeping it up-to-date with recent Kotlin and Java releases 
but have no plans to implement new features. 
Please use the Kotlin Symbol Processing API (KSP) for annotation processing. 
See the list of libraries supported by KSP.
```
- spring data support
  - https://docs.spring.io/spring-data/jpa/reference/repositories/core-extensions.html
```

Querydsl maintenance has slowed down to a point where the community has forked the project 
under OpenFeign at github.com/OpenFeign/querydsl (groupId io.github.openfeign.querydsl). 
Spring Data supports the fork on a best-effort basis.
```

---

### OpenFeign QueryDsl

github
- https://github.com/OpenFeign/querydsl

kapt setting
```kotlin
plugins {
  kotlin("jvm") version "2.2.21"
  kotlin("plugin.spring") version "2.2.21"
  kotlin("plugin.jpa") version "2.2.21"
  id("org.springframework.boot") version "3.5.7"
  id("io.spring.dependency-management") version "1.1.7"
  kotlin("kapt") version "2.2.21"
}

private val queryDslVersion = "7.1"
dependencies {
  implementation("io.github.openfeign.querydsl:querydsl-jpa:$queryDslVersion")
  kapt("io.github.openfeign.querydsl:querydsl-apt:$queryDslVersion:jpa")
}
```

ksp setting
- ksp release: https://github.com/google/ksp/releases
```kotlin
plugins {
  kotlin("jvm") version "2.2.21"
  kotlin("plugin.spring") version "2.2.21"
  kotlin("plugin.jpa") version "2.2.21"
  id("org.springframework.boot") version "3.5.7"
  id("io.spring.dependency-management") version "1.1.7"
  id("com.google.devtools.ksp") version "2.3.0"
}

private val queryDslVersion = "7.1"
dependencies {
  implementation("io.github.openfeign.querydsl:querydsl-jpa:$queryDslVersion")
  ksp("io.github.openfeign.querydsl:querydsl-ksp-codegen:$queryDslVersion")
}
```

---

QueryDsl 7.1 (ksp)
- @QueryProjection 외부 프로퍼티 projection 대상에서 제외
  - https://github.com/OpenFeign/querydsl/pull/1232
- QClass 생성시 value class 의 경우 내부 타입으로 path 생성
  - https://github.com/OpenFeign/querydsl/issues/1403

7.1 ksp 잔존 이슈1
- @QueryProjection 대상에 value class 포함시 QClass 는 내부 타입으로 정상적으로 생성
- ConstructorUtils.getConstructorParameters 에서 매칭된 파라미터 수 비교시 예외 발생
- 매칭 parameter 수 비교 대상에 DefaultConstructorMarker parameter가 포함됨 

<img width="824" height="821" alt="Image" src="https://github.com/user-attachments/assets/75388b23-64e8-4d62-954c-936e09026b3d" />

7.1 ksp 잔존 이슈2
- abstract class 를 상속하는 클래스가 ksp 스캔 대상(@QueryProjection 등) 인 경우 에러
  - `[ksp] java.lang.IllegalStateException: Unable to resolve type of entity for com.study.openfeignquerydsl.dto.BaseDto`
  - @MappedSuperclass 추가시 정상동작
    - 해당 클래스도 QClass 로 생성함
    - QClass 생성도 불필요하고 @MappedSuperclass 를 dto 패키지에 선언 필요
- https://github.com/OpenFeign/querydsl/blob/master/querydsl-tooling/querydsl-ksp-codegen/readme.md
  - excludedClasses 통해 BaseDto 제외해도 에러 동일함
