## spring-event

## @TransactionalEventListener
- since: spring 4.2
- 트랜잭션의 상태에 따라 이벤트 처리 시점을 다르게 하기 위함
- `@EventListener` 의 경우 `publishEvent()` 호출시점에 동기적으로 이벤트를 발행
- 트랜잭션 내 이벤트 발행 메서드 호출 후 예외발생 시 이벤트 리스너의 처리 내용은 롤백되지 않는다.
- 트랜잭션 내에서 이벤트 발행시 트랜잭션의 특정 시점에 이벤트 리스너를 동작시키고자 하는 경우 사용한다.

트랜잭션이 없는 경우 이벤트가 발행되지 않을 수 있다.
```
If the event is not published within an active transaction

https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/event/TransactionalEventListener.html
```

### @TransactionalEventListener Phase

- `@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)`
  - default
  - commit 후 리스너 동작
- `@TransactionalEventListener(phase = TransactionPhase.ROLLBACK)`

- `@TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)`
  - commit or rollback 후
- `@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)`

---

reference
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/event/TransactionalEventListener.html
- https://wildeveloperetrain.tistory.com/246
