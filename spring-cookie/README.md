### Cookie domain 테스트

localhost hosts 파일 수정 (macos)
```
/private/etc/hosts

# 127.0.0.1     localhost
127.0.0.1 example.com
127.0.0.1 test.example.com
```

Cookie, domain 설정
```kotlin
val cookie = Cookie("cookieTestName1", "cookieTestValue1")
cookie.domain = "example.com"
```

테스트1 (domain 미설정 시)
- `example.com` 으로 `cookie` 응답
- `exmaple.com` 에서 재요청 시 `cookie` 들어오는 것 확인
- `test.example.com` 에서 첫 요청시 `cookie` 들어 오지 않고 새로 발급되는 것 확인

테스트2 (domain 설정 시)
- `example.com` 으로 `cookie` 응답
- `exmaple.com` 에서 재요청 시 `cookie` 들어오는 것 확인
- `test.example.com` 에서 첫 요청시 `cookie` 들어오는 것 확인



