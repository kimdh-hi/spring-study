## Spring Cloud


### Eureka discovery server

eureka client 등록시 랜덤포트 (server.port=0) 지정하는 경우
동일 인스턴스 기동시 eureka server 는 동일한 포트로 인식하고 한 개 서비스만 등록한다.
이를 방지하기 위해 eureka server 에 등록하는 이름을 직접 지정한다.
```yml
server.port: 0

eureka:
  instance:
    instance-id: ${spring.cloud.client.hostname}:${spring.application.instance_id:${random.value}}
```