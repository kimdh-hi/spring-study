## @TransactionalEventListener 

트랜잭션의 상태에 따라 이벤트를 발행한다. <br/>
트랜잭션이 성공하고 부가적인 작업시 이벤트를 사용하면 부가적인 작업이 실패해도 이전 성공한 트랜잭션은 롤백되지 않는다. <br/>

#### @EventListener - @TransactionalEventListener


#### Option
`org.springframework.transaction.event`
- `@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)`
  - commit 시 이벤트 발행
- `@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)`
    - commit 전 이벤트 발행
- `@TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)`
  - rollback 시 이벤트 발행
- `@TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)`
  - commit 혹은 rollback 시 이벤트 발행

#### Transaction Propagation
- `Propagation.REQUIRED`
  - default
  - 부모 트랜잭션(기존 트랜잭션)이 없는 경우 새로운 트랜잭션 생성
  - 예외발생시 롤백되고 호출한 곳의 트랜잭션까지 롤백을 전파한다.
- `Propagation.REQUIRES_NEW`
  - 매 번 새로운 트랜잭션을 시작
  - 기존 트랜잭션 내에서 호출된 경우 기존 트랜잭션의 메서드가 종료되고 자신의 트랜잭션을 시작한다. 즉 호출한 곳의 트랜잭션과는 무관한다.
  - 따라서 예외발생시 롤밸을 전파하지 않는다.
- `Propagation.MANDATORY`
  - 부모 트랜잭션 내에서 실행되어야 함.
  - 부모 트랜잭션이 없는 경우 예외 발생

#### 주의
디폴트로 `@TransactionalEventListener` 을 통해 새로운 트랜잭션을 열고 save 를 하는 경우 기존 트랜잭션을 필요로 한다. <br/>
기존 트랜잭션을 만들 수 없거나 만들기 싫은 경우 `Propagation.REQUIRES_NEW` 를 추가해야 한다. <br/>

`UserService.saveByEvent` 억지 예제 참고

---

#### 참고
https://cheese10yun.github.io/event-transaction/ <br/>
https://dev.to/peholmst/knee-deep-in-spring-boot-transactional-event-listeners-and-cglib-proxies-1il9