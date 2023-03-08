## Spring redis expire event

사용 이벤트
- E : `__keyevent@<db>__:`
- x : 만료 이벤트 (키 만료시 생성되는 이벤트)
```
// /etc/redis.conf
// default 는 설정되지 않음

notify-keyspace-events "Ex"
```

위와 같이 설정시 키 만료시 이벤트 수신 토픽 
- `__keyevent@*__:expired`

---

### 참고
http://redisgate.kr/redis/server/event_notification.php
https://backtony.github.io/spring/redis/2021-09-25-spring-redis-3/