## Spring cache

### Spring redis - elastic cache keyspace 설정 에러

redis 서버에 `notify-keyspace-events` 가 설정되지 않은 경우 `spring data redis` 는 `notify-keyspace-events` 설정을 위해 `CONFIG` 명령을 날린다. <br/>

aws elastic cache 는 `CONFIG` 명령을 허용하지 않는다. <br/>

```
The keyspace notification message listener alters notify-keyspace-events settings in Redis, if those are not already set. Existing settings are not overridden, so you must set up those settings correctly (or leave them empty). Note that CONFIG is disabled on AWS ElastiCache, and enabling the listener leads to an error. To work around this behavior, set the keyspaceNotificationsConfigParameter parameter to an empty string. This prevents CONFIG command usage.
```
 
elatic cache 에서 keyspace-event 를 사용하려면 `keyspaceNotificationsConfigParameter = ""`  을 설정해서 `CONFIG` 명령을 redis 서버로 날리는 것을 방지해야 한다. <br/>

```
/**
 * Configure the {@literal notify-keyspace-events} property if not already set. <br />
 * Use an empty {@link String} to keep (<b>not</b> alter) existing server configuration.
 *
 * @return {@literal Ex} by default.
 * @since 1.8
 */
String keyspaceNotificationsConfigParameter() default "Ex";
```

spring data redis 가 `CONFIG` 명려어 날리는 로그 
```
logging:
  level:
    org.springframework.data.redis: DEBUG
    io.lettuce.core: DEBUG

i.lettuce.core.protocol.CommandHandler   : [channel=0x62c509d3, /127.0.0.1:56641 -> localhost/127.0.0.1:6379, epid=0x1, chid=0x1] Completing command AsyncCommand [type=CONFIG, output=MapOutput [output={notify-keyspace-events=xE}, error='null'], commandType=io.lettuce.core.protocol.Command]
```
`keyspaceNotificationsConfigParameter = ""` 설정 시 위 로그 발생 x <br/>

#### 참고
https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis.repositories.expirations

---

## redis sentinel
- redis 고가용성 전략 중 하나 (sentinel, cluster)
- sentinel 노도의 모니터링을 통해 master 노드 장애시 slave 노드를 master 로 승격하여 고가용성 확보

### 구성요소
- sentinel: master/replica node 모니터링 및 자동 장애 복구
- master(primary)
- slave(replica)

### 장애판별

#### SDOWN (Subjectively Down, 주관적 장애)
- sentinel -> master 로 ping/info 응답이 3초(down-after-millisecondes) 간 오지 않는 경우 SDOWN 으로 판단.
- SDOWN 만으로 장애조치는 진행되지 않는다.

#### ODOWN (Objectively Down, 객관적 장애)
  - quorum 이상 수의 sentinel 이 해당 마스터가 장애라고 판단하는 경우 ODOWN으로 판단되어 장애조치 진행

```
sentinel 수와 quorum
- quorum 이 2인 경우 최소 2대 이상의 sentinel 이 master 장애로 인지해야 장애복구 진행

최소 권장사항
- sentinel 3대
- quorum 2
```

### master 선출
- ODOWN 시 새로운 master 선출
- SDOWN/ODOWN/DISCONECT 상태 slave 는 선출 대상에서 제외
- 남은 slave 중 `slave_prority` 가 높은 순으로 선택 (default: 100)
  - `slave_prority` 를 0을 설정한 node 는 master 승격 대상에서 제외

### spring data redis sentinel connection

#### code base

```
@Bean
public RedisConnectionFactory lettuceConnectionFactory() {
  RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
  .master("mymaster")
  .sentinel("127.0.0.1", 26379)
  .sentinel("127.0.0.1", 26380);
  return new LettuceConnectionFactory(sentinelConfig);
}

```

#### property base

```
RedisSentinelConfiguration can also be defined through RedisSentinelConfiguration.of(PropertySource), which lets you pick up the following properties:

Configuration Properties
- spring.redis.sentinel.master: name of the master node.
- spring.redis.sentinel.nodes: Comma delimited list of host:port pairs.
- spring.redis.sentinel.username: The username to apply when authenticating with Redis Sentinel (requires Redis 6)
- spring.redis.sentinel.password: The password to apply when authenticating with Redis Sentinel
- spring.redis.sentinel.dataNode.username: The username to apply when authenticating with Redis Data Node
- spring.redis.sentinel.dataNode.password: The password to apply when authenticating with Redis Data Node
- spring.redis.sentinel.dataNode.database: The database index to apply when authenticating with Redis Data Node
```

----

issue
- sentinel monitoring > redis-master not found
  - https://stackoverflow.com/questions/57464443/redis-sentinel-throws-error-cant-resolve-master-instance-hostname
  - sentinel resolve-hostnames yes

---

reference
- https://redisgate.kr/redis/clients/spring_session_redis_sentinel.php