## kotlin-logging

java String concat logging
- 내부적으로 `String.valueOf(userId)`, `String.valueOf(username)` 연산 발생
- 문자열 변환 후 문자열 concat 연산
- info, warn, error 로그레벨로 설정한 경우에도 문자열 연산 발생 (debug 임에도)  
```java
logger.debug("user not found. userId = " + userId + "username = " + username);
```

java Parameterized logging
- {} 부분은 해당 로깅레벨이 활성화 된 경우에만 치환되므로, 불필요한 `String.valueOf(userId)`, `String.valueOf(username)` 연산 방지
```java
logger.debug("user not found. userId = {}, username = {}", userId, username);
```

kotlin string template
- `$` 를 사용하는 kotlin string template 의 경우 내부적으로 `toString()` 을 호출한다.
- java string concat logging 과 동일하게 `toString` 이후 문자열 concat 연산이 설정된 로그레벨과 무관하게 발생된다.
- but, 가독성이 매우 좋음
```kotlin
logger.debug("user not found. userId = $userId, username = $username");
```

---

reference
- https://tech.kakaopay.com/post/efficient-logging-with-kotlin/
