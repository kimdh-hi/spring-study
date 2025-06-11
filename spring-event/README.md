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
  - listener 에서 예외발생시 이벤트를 발행한 트랜잭션은 rollback 되지 않는다.
- `@TransactionalEventListener(phase = TransactionPhase.ROLLBACK)`

- `@TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)`
  - commit or rollback 후
- `@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)`
  - listener 에서 예외발생시 이벤트를 발행한 트랜잭션은 rollback 된다.

### @TransactionalEvent, Propagation.REQUIRES_NEW
- `@TransactionalEvent` 이 선언된 리스너에서 insert, delete, update 시 transaction propagation 은 `REQUIRES_NEW` 로 선언되어야 한다. 
  - TransactionSynchronization.afterCommit 주석 참조
```
The transaction will have been committed already, but the transactional resources might still be active and accessible. 
As a consequence, any data access code triggered at this point will still "participate" in the original transaction, 
allowing to perform some cleanup (with no commit following anymore!), unless it explicitly declares that it needs to run in a separate transaction. 
Hence: Use PROPAGATION_REQUIRES_NEW for any transactional operation that is called from here.

// 기존 트랜잭션에 참여하지만, 이미 commit 된 트랜잭션에 대해 다시 커밋하지 않는다.
// `PROPAGATION_REQUIRES_NEW` 를 사용해서 새로운 트랜잭션을 열어라.
```
- `Propagation.REQUIRES_NEW` 를 설정하는 방식 외
  - `TransactionPhase.BEFORE_COMMIT` 지정
  - `@Async` 를 통해 다른 tread 에서 트랜잭션이 commit 되도록 하는 방법


---

reference
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/event/TransactionalEventListener.html
- https://findstar.pe.kr/2022/09/17/points-to-consider-when-using-the-Spring-Events-feature/
