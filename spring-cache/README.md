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