## Actuator

```
현재 설정으로 actuator 가 제공하는 엔드포인트 확인
localhost:8080/actuator
```

---

### health
```yaml
management:
  endpoint:
    health:
      #show-details: always # 수치까지 확인
      show-components: always # 체크해야 할 각 컴포넌트의 상태만 표시
```
요청에 대한 응답, 데이터베이스, 디스크 사용량 등을 체크해서 모두 정상인 경우 `UP` 을 표시한다.

---

### info
```yaml
management:
  info:
    java:
      enabled: true
    os:
      enabled: true
```
`management.endpoint.info` 아님 주의 <br/>\
기본적으로 java, os 에 대한 정보는 기본적으로 disable 되어있다.<br/>

`빌드 정보 노출` <br/>

빌드 시점에 `META-INF/build-info.properties` 파일을 만들어야 한다. <br/>
gradle 의 경우 아래 코드를 추가하면 된다.<br/>
```
springBoot {
  buildInfo()
}
```

결과화면 <br/>
```
{
  "build": {
    "artifact": "actuator",
    "name": "actuator",
    "time": "2023-04-07T01:23:14.572Z",
    "version": "0.0.1-SNAPSHOT",
    "group": "hello"
  },
  "java": {
    "version": "17.0.3",
    "vendor": {
      "name": "Azul Systems, Inc.",
      "version": "Zulu17.34+19-CA"
    },
    "runtime": {
      "name": "OpenJDK Runtime Environment",
      "version": "17.0.3+7-LTS"
    },
    "jvm": {
      "name": "OpenJDK 64-Bit Server VM",
      "vendor": "Azul Systems, Inc.",
      "version": "17.0.3+7-LTS"
    }
  },
  "os": {
    "name": "Mac OS X",
    "version": "13.0",
    "arch": "aarch64"
  }
}
```

`git 정보노출` <br/>
배포된 소스의 브랜치, 커밋넘버 등을 확인할 수 있다.<br/>

플러그인 추가
```
id 'com.gorylenko.gradle-git-properties' version '2.4.1' // git info
```
`build/resources/main/git.properties` 에 생성결과 확인 <br/>

---

### logger
- 로그레벨 확인
- 실시간 로그 레벨 변경
```http request
POST http://localhost:8080/actuator/loggers/[package_path]
Content-Type: application/json

{
  "configuredLevel": "DEBUG"
}
```

---

### httpexchanges
- http 요청/응답 기록 확인

`HttpExchangeRepository` 구현체를 빈으로 등록해줘야 한다.<br/>
```java
@Bean
public InMemoryHttpExchangeRepository httpExchangeRepository() {
  return new InMemoryHttpExchangeRepository();
}
```
`InMemoryHttpExchangeRepository` 의 경우 기본적으로 100개까지 확인 가능하고 초과시 과거요청은 삭제된다. (요청수 변경가능 `setCapacity()`) <br/>

---

### metrics

tomcat 메트릭 수집
```yaml
server:
  tomcat:
    mbeanregistry:
      enabled: true
```
- `tomcat.threads.config.max`
  - tomcat thread 가 동시에 처리할 수 있는 요청 수
- `tomcat.threads.busy`
  - 실제 처리중인 thread 수 

---

## prometheus
```
실행
./prometheus => localhost:9090
```

프로메테우스는 메트릭을 수집하고 보관하는 DB 역할을 수행한다. <br/>
우리 애플리케이션에서 발생하는 메트릭들을 프로메테우스가 수집하도록 연동을 필요로 한다.<br/>

프로메테우스는 `/actuator/metrics` 조회시 응답되는 json 형태 데이터 이해하지 못한다. 이를 프로메테우스가 이해할 수 있도록 변환하는 작업 등을 `마이크로미터`가 수행한다.<br/>
```
//마이크로미터-프로메테우스 의존성 추가
implementation 'io.micrometer:micrometer-registry-prometheus'
```
새로 추가되는 actuator 엔드포인트 확인 `/actuator/prometheus` <br/>

포멧 차이
- `/avtuator/metrics/jvm.info` -> `/actuator/prometheus => jvm_info`


