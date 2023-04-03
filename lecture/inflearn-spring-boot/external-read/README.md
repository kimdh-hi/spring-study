## Spring boot read property

---

#### @Value 로 프로퍼티를 읽는 경우 기본값 사용
```kotlin
@Value("${my.key1:default}") var key1: String
```