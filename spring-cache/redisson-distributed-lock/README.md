### Redisson 분산 락

Redisson은 Jedis, Lettuce와 같은 레디스 클라이언트의 일종이다.

Redisson 의 `tryLock` 메서드는 타임아웃 설정이 가능하다.

```kotlin
// waitTime: 락 획득을 대기시간에 대한 타임아웃
// leaseTime: 락에 대한 타임아웃
public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException
```

- `waitTime` 이 경과될 동안 락을 잡지 못하면 false가 반환되고 락 획득 시도를 멈춘다.
- `leaseTime` 이 경과되면 락이 만료된다. 별도의 락 해제 작업이 필요없다.

Redisson은 스핀 락을 사용하지 않는다.

- Redisson 은 `pub-sub` 구조이기 때문에 subscriber(클라이언트)에게 락 획득을 시도할 수 있는 알림을 주는 것으로 `락 획득 가능 시점` 을 알릴 수 있다.
- 스핀 락과 같이 락을 획득할 수 있는지 시간단위로 계속해서 확인하지 않아도 된다.

---

### Lettuce 를 이용한 분산 락 문제점

레디스 클라이언트 중 Lettuce 도 락을 지원한다.<br/>
Lettuce 는 `setnx` 명령을 통해 락을 획득한다.<br/>
레디스에 값이 존재하지 않으면 값을 세팅하고, 값이 세팅되었는지 여부를 리턴받아 락을 획득하는데 성공했는지 확인한다.

락을 획득하면 로직을 수행하고 해제한다.

---

Lettuce 의 분산 락은 다음과 같은 문제가 있다.

Lock 타임아웃을 설정할 수 없다.

- 루프를 돌며 계속해서 락 획득을 시도하는데 특정 애플리케이션이 락을 작은 채로 죽는다면 해당 락은 해제되지 않는다.
- 다른 애플리케이션은 해당 락을 잡기위해 무한정 대기하게 된다.

위와 같은 문제를 방지하기 위해 락에는 타임아웃이 설정되어야 한다.

계속해서 락 획득을 시도하는 `스핀 락` 형태는 레디스에 큰 부담을 준다.