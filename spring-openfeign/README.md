## Spring OpenFeign

재시도
- default: Retryer.NEVER_RETRY (재시도 안 함)
- Feign 에 제공하는 Retryer는 IO exception 이 발생한 경우에만 동작
  - IO exception 외 다른 예외에 대해서 재시도를 원한다면 spring-retry 사용 등 다른 설정 필요

---

### 참고
https://engineering.getmidas.com/using-spring-retryable-with-feign-client-methods-9f77e509ad55 <br/>
