# server.forward-headers-strategy

## server.forward-headers-strategy

- was가 리버스 프록시 뒤에 있는 경우, 프록시가 붙여준 원본 요청 정보 헤더(X-Forwarded-For|Proto|Host|Port..)에 대한 was 측에서의 사용여부와 신뢰 조건에 대한 설정
- 결과적으로 `request.getRemoteAddr()` 시 실제 client의 ip 가 응답되는지, 리버스 프록시의 ip가 응답되는지를 결정
    - getServerName(), getServerPort() 등 메서드도 동일
- `server.forward-headers-strategy: none`
    - default
    - `X-Forwarded-*` 헤더 무시
    - `request.getRemoteAddr()` 시 리버스 프록시의 ip 응답
- `server.forward-headers-strategy: native`
    - `X-Forwarded-*` 헤더 사용
    - Tomcat 수준에서 `X-Forwarded-*` 값으로 바꿈 (RemoteIpValve)
- `server.forward-headers-strategy: framework`
    - https://docs.spring.io/spring-framework/reference/web/webmvc/filters.html#filters-forwarded-headers-security
    - `X-Forwarded-*` 헤더 사용
    - spring filter 수준에서 `X-Forwarded-*` 값으로 바꿈 (ForwardedHeaderFilter)

## native, framework 차이

- 단순히 어떤 레이어에서 `X-Forwarded-*` 의 값을 바꾸는지 보다 기능적인 차이를 알아야 함
- 기능적으로 크게 2가지 차이가 있음

### 차이1

- 표준 Forwarded 헤더, X-Forwarded-Prefix, X-Forwarded-Ssl 은 framework 에서만 사용 가능. native 는 지원안함

### 차이2

- framework 는 별도 추가 검증 과정없이 `X-Forwarded-*` 으로 값을 바꿈
- 반면, native의 경우 was에 tcp 연결을 한 직전 상대(일반적으로 리버스 프록시)의 ip대역에 따라 `X-Forwarded-*` 헤더값 사용여부를 결정 가능
- 허용 ip 대역은 internal-proxies 통해 설정 가능. (internal-proxies 는 정규식으로 설정)
- internal-proxies 설정하지 않고 native 만 설정하는 경우 기본적으로 사설 ip 대역 전체를 허용 (loopback 등 추가적으로 허용되는 것 더 있음)
- `X-Forwarded-*` 헤더 변조 위험성, 출처에 대한 신뢰 관점에서 native 가 보안상 우수

## 무조건 natvie 인가?

- 신뢰 경계에 있는 리버스 프록시가 `X-Forwarded-*` 헤더를 신뢰할 수 있도록 하는 처리를 담당하고 was 는 그대로 받아서 사용할 수 있어야 한다.
    - https://docs.spring.io/spring-framework/reference/web/webmvc/filters.html#filters-forwarded-headers-security
- spring 문서에 의하면 필요하면 `server.forward-headers-strategy: framework` 를 사용하고 클라이언트 측에서 임의로 넣은 `X-Forwarded-*` 헤더에 대한 덮어쓰기, 제거 등의 처리는 리버스 프록시가 담당해야 한다.

## 결론

- 리버스 프록시에서 `X-Forwarded-*` 헤더에 대한 정리 작업은 전략 관계없이 반드시 필요함
- 모든 요청이 리버스 프록시를 통해서만 was 에 도착하는 경우 framework 와 native 에 보안적인 측면의 차이는 없음
- 단, was 에 직접 접근 가능한 어떤 경로가 열려있는 경우 차이가 발생
- 어떤 이유로든 잘못된 `X-Forwarded-*` 사용 방지를 위해 기본적으로 지정한 리버스 프록시 ip가 주는 `X-Forwarded-*` 만 사용 가능한 native 를 기본으로 사용하는 것이 좋을 듯

```yaml
server:
  forward-headers-strategy: native
  tomcat:
    remoteip:
      internal-proxies: "10\\.5\\.0\\.1"
```

## 리버스 프록시 설정 (haproxy)

- `http-request del-header X-Forwarded-For`
    - 리버스 프록시로 요청의 XFF 헤더 제거
    - 클라이언트가 임의로 넣은 XFF 헤더 제거 위함
- `option forwardfor`
    - 클라이언트 IP를 값으로 하는 XFF(X-Forwarded-For) 헤더 추가

## reference

- https://docs.spring.io/spring-boot/how-to/webserver.html#howto.webserver.use-behind-a-proxy-server
- https://docs.spring.io/spring-framework/reference/web/webmvc/filters.html#filters-forwarded-headers
- https://tomcat.apache.org/tomcat-11.0-doc/config/valve.html#Remote_IP_Valve
- https://www.haproxy.com/documentation/haproxy-configuration-tutorials/proxying-essentials/client-ip-preservation/add-x-forward-for-header/