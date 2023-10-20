## Spring Cloud Config

### config server 변경사항 적용

- 서버 재기동
- spring actuator refresh
    - 각 application 마다 refresh 필요
- spring cloud bus
    - spring cloud config server + spring cloud bus + messaging system(amqp, kafka)

```
# config server, rabbitmq (cloud-bus) 실행
docker-compose up -d
```

### 프로퍼티 갱신
```
POST http://localhost:8881/actuator/busrefresh
```

### 프로퍼티 대칭키 암호화
config server, client 대칭키 암호화를 위한 키 추가<br/>
`encrypt.key=key`

암/복호화 시 config server api 호출
```
POST localhost:8881/encrypt
POST localhost:8881/decrypt
```

암호화된 프로퍼티에 prefix 추가<br/>
`yml` 의 경우 '' 로 묶어줘야 함
```yml
custom:
  enc: '{cipher}4ef0ea563b3f3f41a071d4a793f31be892971b24538c7cd70e3fd98c3a59afd6'
```

### ++
```
spring boot 3.0.0 이상 

Improved @ConstructorBinding Detection

When using constructor bound @ConfigurationProperties the @ConstructorBinding annotation is no longer required if the class has a single parameterized constructor. 
If you have more than one constructor, you’ll still need to use @ConstructorBinding to tell Spring Boot which one to use.

- 하나의 생성자만 있다면 @ConstructorBinding 제거 (클래스 레벨에 사용불가)
- 두 개 이상의 생성자가 있다면 메서드 레벨에 어노테이션 추가해줘야 함
```


### 참고

https://github.com/hyness/spring-cloud-config-server

https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0.0-M2-Release-Notes

@ConstructorBinding cloud config 갱신 안 됨 이슈
https://github.com/spring-cloud/spring-cloud-commons/issues/1029
https://github.com/spring-cloud/spring-cloud-config/issues/1547