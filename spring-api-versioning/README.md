## API versioning

### 버전관리 방법

1. URL
```kotlin
// http://localhost:8080/api/v1/test
// http://localhost:8080/api/v2/test
@RestController
@RequestMapping("/api")
class TestController {
  
  @GetMapping("/v1/test")
  fun testV1() = "testV1"
  
  @GetMpping("/v2/test")
  fun testV2() = "testV2"
}
```

2. Request Parameter
```kotlin
// http://localhost:8080/api/test?version=1
// http://localhost:8080/api/test?version=2
@RestController
@RequestMapping("/api")
class TestController {
  
  @GetMapping("/test", params = "version=1")
  fun testV1() = "testV1"

  @GetMapping("/test", params = "version=2")
  fun testV2() = "testV2"
}
```

3. Header
- `X-API-VERSION` 은 표준 HTTP 헤더는 아니고 커스텀하게 만들어서 사용하면 됨
- Github => `X-GitHub-Api-Version`
```kotlin
@RestController
@RequestMapping("/api")
class TestController {
  
  @GetMapping("/test", headers = "X-API-VERSION=1")
  fun testV1() = "testV1"

  @GetMapping("/test", headers = "X-API-VERSION=2")
  fun testV2() = "testV2"
}
```

---

### 참고
https://docs.github.com/en/rest/overview/api-versions?apiVersion=2022-11-28 <br/>
https://stackoverflow.com/questions/20198275/how-to-manage-rest-api-versioning-with-spring/21176971#21176971 <br/>
