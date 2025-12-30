## jpa-core

### TransactionSynchronizationManager
- transaction commit 이후 동작 지정에 용이
- @TransactionalEventListener 로 별도 이벤트로 분리하지 않고 동일 함수 안에서 모두 처리하고자 하는 경우 사용

```kotlin
  @Transactional
  fun save(name: String): User {
    val user = userRepository.save(User.of(name))

    TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
      override fun afterCommit() {
        log.debug("send welcome email...")
      }
    })

//    runAfterCommit {
//      log.debug("send welcome email...")
//    }

//   TransactionDelegator.runAfterCommit {
//      log.debug("send welcome email...")
//   }

    return user
  }
```

```kotlin
// kotlin dsl 사용.
// 별도 네임스페이스가 없어서 아래 코드 존재를 모르는 사람은 찾아 사용하기 힘들 듯.
fun runAfterCommit(action: () -> Unit) {
  if (TransactionSynchronizationManager.isSynchronizationActive()) {
    TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
      override fun afterCommit() {
        action()
      }
    })
  } else {
    action()
  }
}

// 네임스페이스가 필요하다면.
object TransactionDelegator {
   fun runAfterCommit(action: () -> Unit) {
      if (TransactionSynchronizationManager.isSynchronizationActive()) {
         TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
            override fun afterCommit() {
               action()
            }
         })
      } else {
         action()
      }
   }
}

```

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

### circular-wait deadlock

deadlock 발생 조건
- 아래 4가지 모두 만족하는 경우 deadlock 발생

1. 상호배제
2. 점유대기
3. 비선점
4. 순환대기

순환대기
- sessionA: A자원 lock 점유, B자원 lock 대기
- sessionB: B자원 lock 점유, A자원 lock 대기

순환대기 발생 케이스 
- CircularWaitDeadlockTestService.kt

| tx1              | tx2              |
|------------------|------------------|
| userA xlock 점유   |                  |
|                  | deviceA xlock 점유 |
| deviceA xlock 대기 |                  |
|                  | userA xlock 대기   |

- *deviceA lock(x) 대기, userA lock(x) 대기* 

해결 방안
- User -> Device 순 갱신

| tx1                   | tx2                |
|-----------------------|--------------------|
| userA xlock 점유        |                    |
|                       | userA xlock 대기     |
| deviceA xlock 점유      |                    |
|                       | userA xlock 대기     |
| commit / lock release |                    |
|                       | userA xlock 점유     |
|                       | deviceA xlock 점유   |
|                       | commit / lock release |

```kotlin
@Service
class CircularWaitDeadlockTestService(
  private val userRepository: UserRepository,
  private val deviceRepository: DeviceRepository,
) {
  @Transactional
  fun txUserFirst(userId: String, deviceId: String) {
    val user = userRepository.findById(userId).get()
    user.updateName("txUserFirst")

    Thread.sleep(100) // for test

    val device = deviceRepository.findById(deviceId).get()
    device.updateKey("txUserFirstKey")
  }

   //호출 순서 변경시 deadlock safe
  @Transactional
  fun txDeviceFirst(userId: String, deviceId: String) {
    val user = userRepository.findById(userId).get()
    user.updateName("txDeviceFirst")

     Thread.sleep(100) // for test

    val device = deviceRepository.findById(deviceId).get()
    device.updateKey("txDeviceFirstKey")
  }
}

```

### 
