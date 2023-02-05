## Spring cloud stream



spring cloud stream 3.0부터 어노테이션 기반 설정 deprecated<br/>
Function 을 사용하는 방식으로 사용해야 함


---

input/output 규칙

- input: `함수명`-in-`index` 
- output: `함수명`-out-`index` 

Functional 방식의 경우 @Bean 으로 등록한 Supplier, Function Consumer 의 함수명을 기반으로 바인딩을 생성한다.

- destination: exchange or topic
- bindings: 채널정보 (rabbitmq-exchange, kafka-topic)
- binders: 메시지 브로커 (rabbitmq, kafka)

---

참고

https://spring.io/projects/spring-cloud-stream

