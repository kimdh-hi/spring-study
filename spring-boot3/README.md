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
