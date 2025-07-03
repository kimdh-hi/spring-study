## jpa-core

### LazyConnectionDataSourceProxy
- 일반적으로 @Transactional 사용시 aop 를 통해 해당 메서드 실행시 db connection 을 맺게 된다.
- @Transactional 가 적용된 메서드 내에 외부 I/O 등 무거운 연산이 있는 후 쿼리하는 경우 외부 I/O 동안에도 db connection 을 점유하게 된다.
- LazyConnectionDataSourceProxy 는 실제 쿼리가 필요할 때까지 db connection 을 유예시킨다.

#### @Transactional flow
1. TransactionInterceptor
2. TransactionAspectSupport
3. TransactionManager determine (JpaTransactionManager)
4. Transaction 생성 및 thread bind
5. JpaTransactionManager doBegin()
   6. DataSource connection get
6. JpaTransactionManager doCommit()
   7. DataSource commit

LazyConnectionDataSourceProxy 적용시 `prepareStatement` 시점에 실제 DataSource 접근

```java
//MutationStatementPreparerImpl
protected final Connection connection() {
  return logicalConnection().getPhysicalConnection(); // LazyConnectionInvocationHandler 반환
}

//HikariDatSource.getConnection() 호출
```

---


