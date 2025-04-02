## custom repository implementations

custom repository 의 구현체를 생성하는 경우 해당 custom repository interface 이름에 접미사로 `prefix` 를 붙인다.

```
The most important part of the class name that corresponds to the fragment interface is the Impl postfix.
```

### single custom implementation
- single custom implementation -> fragment based pattern migration 권장
```kotlin
// single custom implementation 비권장 방식 (UserRepositoryImpl)
interface UserRepository : JpaRepository<User, String>, UserRepositoryCustom

interface UserRepositoryCustom {
	//...
}

interface UserRepositoryImpl : UserRepositoryCustom {
	//...
}

```

```kotlin
// 권장 (UserRepositoryCustomImpl)
interface UserRepository : JpaRepository<User, String>, UserRepositoryCustom

interface UserRepositoryCustom {
	//...
}

interface UserRepositoryCustomImpl : UserRepositoryCustom {
	//...
}

```

---

### reference
- https://docs.spring.io/spring-data/jpa/reference/repositories/custom-implementations.html
- https://docs.spring.io/spring-data/commons/docs/1.9.0.RELEASE/reference/html/#repositories.single-repository-behaviour