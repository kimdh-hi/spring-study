## @TransactionalEventListener 

트랜잭션의 상태에 따라 이벤트를 발행한다. <br/>
트랜잭션이 성공하고 부가적인 작업시 이벤트를 사용하면 부가적인 작업이 실패해도 이전 성공한 트랜잭션은 롤백되지 않는다. <br/>

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