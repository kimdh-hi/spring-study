## RestClient

- spring 6.1, spring boot 3.2.x 사용 가능
- webClient 와 매우 유사
  - `webflux 의존성 불필요`
  - `Mono`, `Flux` x
  - 다만, webClient 때문에 추가된 webflux 의존성 제거시 빌드 속도에는 큰 차이가 없다고 함.
- spring 6 에 도입된 httpInterface 과 결합 가능


---

- https://spring.io/blog/2023/07/13/new-in-spring-6-1-restclient
- https://docs.spring.io/spring-framework/reference/integration/rest-clients.html


