## Springboot3.x 

### spring security

EnableGlobalMethodSecurity deprecated
- Deprecated Use EnableMethodSecurity instead
- `@EnableGlobalMethodSecurity(prePostEnabled = true)` -> `@EnableMethodSecurity(prePostEnabled = true)`

---

### spring integration

IntegrationFlows deprecated
```kotlin
// deprecated
  @Bean
  fun flow(): IntegrationFlow = IntegrationFlows.from(
    Amqp.inboundAdapter(SimpleMessageListenerContainer(...))
  )
    .handle(activator, "activatorFunction")
    .get()

```

```kotlin
  @Bean
  fun flow(): IntegrationFlow = integrationFlow(SimpleMessageListenerContainer(...)) {
    handle(activator, "activatorFunction")
  }
```

---

### JPA

3.2.0 이전 Enum data type 이슈
- 엔티티에서 `enum` 필드 사용시 db data type 으로 `ENUM` 이 사용됨
  - `ENUM` type 의 경우 enum 타입 추가시 스키마가 변경되는 등 문제가 있음
- 3.2.0 이후 `@JdbcTypeCode(SqlTypes.VARCHAR)` 사용시 `varchar(255)` 로 스키마 생성됨