# Spring Actuator

## actuator 적용시 검토사항

### 포트, 바인딩 주소 분리
- `management.server.port`, `management.server.address`
    - `management.server.address` 는 포트가 분리된 경우에만 적용되므로 반드시 `management.server.port` 와 함께 적용되어야 함.
    - 추가로 `management.server.port` 가 server.port 와 달라야 `management.server.address` 적용됨
- 동일 JVM 내 내장톰캣 두 개 구동
    - 메인 Application(8080), Actuator application(8081)
- 위 설정 미설정시 기본적으로 springboot 내장톰캣이 뜨는 동일 주소, 포트에 actuator 엔드포인트가 서비스 됨.
- 즉, 리버스 프록시가 중계하는 외부 요청 경로에 actuator 엔드포인트도 같이 놓인다.
- actuator 엔드포인트로 외부에서 접근이 가능한 경우 실수로 env, heapdump, configprops 등이 열려있으면 외부에서 전부 접근 가능하므로 보안상 취약
    - 기본 노출은 health 뿐이지만 설정상 실수 또는 필요에 의해 env, heapdump, configprops 등이 열릴 수 있음
- 배포 환경에 따라 축소해서 endpoint를 열어두겠지만 의도치 않은 실수가 생길 수 있음
- 이를 방지하기 위해 근본적으로 외부에서 접근 가능한 경로를 제거하기 위해 actuator 엔드포인트 서비스 주소 및 포트를 분리
- ex) 동일 포트에 서비스되어 앞단 리버스 프록시 타고 외부 요청이 들어오지 않도록 포트 분리를 통해 접근 가능 경로 자체를 제거
- ex) alloy 사용시 동일 서버에 alloy 프로세스가 있는 경우 127.0.0.1(로컬) 접근만을 허용


```
server:
  port: 8080                         

management:
  server:
    port: 8081 # actuator 엔드포인트 서비스 포트 분리 (default: main application 포트와 동일)
    address: "127.0.0.1" # 해당 ip 로 들어오는 연결만 받음 (바인딩 주소, default: 0.0.0.0)
  endpoints:
    web:
      exposure:
        include: "health,prometheus"
    access:
      max-permitted: read-only # @ReadOperation 만 허용
```

## reference
- https://docs.spring.io/spring-boot/reference/actuator/monitoring.html
- claude