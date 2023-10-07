## Spring Micrometer, Grafana

Why Micrometer?
- cpu 사용량, jvm 커넥션 등 지표를 모니터링 툴이 제공하는 api 를 통해 모니터링 툴로 전달해야 함
- JMX 모니터링 툴을 사용하고 있는 경우 JMX 가 제공하는 api 로 우리 애플리케이션의 사용지표를 전달할 것임
- 모니터링 툴을 jmx 에서 프로메테우스로 변경하고 싶다면?
  - 기존 jmx api 에서 프로메테우스가 제공하는 api 로 변경해야 함
- 마이크로미터는 위와 같은 문제를 해결한다.	
- 마이크로미터는 여러 지표에 대한 추상화 된 표준 측정 방식을 제공하고 이에 대해 각 모니터링 툴에 대한 구현체를 제공한다.
- Spring boot actuator 는 마이크로미터를 내장한다.

---

spring actuator metric 확인
- endpoint:  `/actuator/metrics`, `/actuator/metrics/{name}`

```
http 요청수 확인

/actuator/metrics/http.server.requests

특정 엔드포인트 요청확인
http://localhost:8080/actuator/metrics/http.server.requests?tag=uri:/test&tag=status:200
```

tomcat metric 활성화
```yaml
server.tomcat.mbeanregistry.enabled: true
```

---

프로메테우스 설정

```kotlin
implementation("io.micrometer:micrometer-registry-prometheus")
```

메트릭 접근
- `/actuator/prometheus`

