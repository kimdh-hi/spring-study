## Springboot 3.x 

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

Enum data type 이슈 2
- `@JdbcTypeCode(SqlTypes.VARCHAR)` 지정시 data type 은 `varchar` 로 정의되지만 enum 에 대한 제약조건이 붙는다.
- 즉, ENUM data type 을 그대로 사용하는 것과 다를게 없다.
```
create table tb_user (
    id varchar(255) not null,
    name varchar(255),
    user_type varchar(255) check (user_type in ('USER','ADMIN')),
    primary key (id)
) engine=InnoDB
```

---

### webclient
- https://docs.spring.io/spring-boot/reference/io/rest-client.html#io.rest-client.webclient.configuration
  - webclient auto-configuration
```yaml
spring:
  http:
    reactiveclient:
      connect-timeout: 3s
      read-timeout: 30s
```
