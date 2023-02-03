## Spring Cloud Config

```
# config server, rabbitmq (cloud-bus) 실행
docker-compose up -d
```

```
프로퍼티 갱신

POST http://localhost:8881/actuator/busrefresh
```



### 참고

https://github.com/hyness/spring-cloud-config-server