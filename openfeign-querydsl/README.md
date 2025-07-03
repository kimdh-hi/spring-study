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
private val queryDslVersion = "7.0"
dependencies {
  implementation("io.github.openfeign.querydsl:querydsl-jpa:$queryDslVersion")
  kapt("io.github.openfeign.querydsl:querydsl-apt:$queryDslVersion:jpa")
}
```

ksp setting
- ksp release: https://github.com/google/ksp/releases
```kotlin
plugins {
  id("com.google.devtools.ksp") version "2.1.21-2.0.2"
}

private val queryDslVersion = "7.0"
dependencies {
  implementation("io.github.openfeign.querydsl:querydsl-jpa:$queryDslVersion")
  ksp("io.github.openfeign.querydsl:querydsl-ksp-codegen:$queryDslVersion")
}
```


