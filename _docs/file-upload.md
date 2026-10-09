# springboot 파일 업로드 처리

## multipart

1. 클라이언트는 tomcat 으로 tcp 연결을 맺고 tomcat acceptor 가 이를 수락하고 poller 에 등록
2. 클라이언트는 요청 헤더와 본문을 전송 (헤더 전송 이후 서버 응답 대기하지 않고 곧바로 이어 본문 전송)
3. poller 는 읽을 데이터를 감지하고 스레드 풀의 워커스레드에 작업 할당
4. 워커 스레드는 요청 헤더 소켓으로부터 데이터 읽어 요청 헤더 파싱 후 필터체인 실행 (이 때, 요청 본문 대부분은 커널 수신 버퍼에 위치하거나 아직 클라이언트로부터 전송중)
5. DispatcherServlet 도착, multipart 요청인 경우 MultipartResolver 호출되어 서블릿 api(request.getParts()) 호출
6. tomcat 워커스레드가 multipart 본문 읽기 시작 (multipart 파서가 수행)
7. multipart 파서는 본인 버퍼를 채우기 위해 ServletInputStream.read() 호출하여 tomcat 읽기 버퍼(8kb) 로부터 데이터 복사받음. 이 때 tomcat 버퍼에 데이터가 없는 경우 커널 수신 버퍼로부터 읽어서 버퍼 채움
8. multipart 파서는 본인 버퍼의 데이터 대상 multipart 바운더리 체크 및 임시파일 write 수행
9. 임시 파일 쓰기가 완료된 이후 controller 호출
10. multipart 임시파일은 컨트롤러 응답 반환 이후 cleanup (DispatcherServlet finally 블록에서 처리)

### spring.servlet.multipart.file-size-threshold

- https://docs.spring.io/spring-boot/api/java/org/springframework/boot/servlet/autoconfigure/MultipartProperties.html
- default: 0
- multipart 요청의 한 개 part 크기가 위 설정을 초과하는 경우 디스크에 저장 (임시 디렉터리의 파일로 기록)
- 즉, 기본 설정인 경우 실제 파일의 bytes 에 접근(getBytes)하지 않는 경우 메모리 수준으로 전체 파일 올라오지 않음
- Tomcat 은 소켓으로부터 작은 버퍼 단위로 읽어서 임시 파일에 계속 써 나가기 때문에 버퍼 크기 정도만 메모리를 사용
- controller 의 인자로 받는 MultipartFile 은 해당 임시 파일을 가리키는 객체
- 위 값 커스텀 필요한 경우
    - 작은 파일 업로드가 대부분이고 요청이 많은 경우 굳이 임시파일로 만들지 않고 메모리 상에서 처리하여 성능 향상 위함

### spring.servlet.multipart.location

- 임시파일 저장 경로
- default: null
- 지정하지 않는 경우 임시 디렉터리 사용
- `/tmp/tomcat.<난수>.<포트>/work/Tomcat/localhost/ROOT/...`

### transferTo

- transferTo 통해 임시 파일을 동일 파일 시스템으로 이동하는 경우 임시 파일이 아니게 되어 삭제되지 않음.
- 동일 파일 시스템 저장이 목적이라면 임시 파일을 그대로 이동시키는 것이므로 I/O 추가적인 부하 없음

---

## raw body

1. 클라이언트는 tomcat 으로 tcp 연결을 맺고 tomcat acceptor 가 이를 수락하고 poller 에 등록
2. 클라이언트는 요청 헤더와 본문을 전송 (헤더 전송 이후 서버 응답 대기하지 않고 곧바로 이어 본문 전송)
3. poller 는 읽을 데이터를 감지하고 스레드 풀의 워커스레드에 작업 할당
4. 워커 스레드는 요청 헤더 소켓으로부터 데이터 읽어 요청 헤더 파싱 후 필터체인 실행 (이 때, 요청 본문 대부분은 커널 수신 버퍼에 위치하거나 아직 클라이언트로부터 전송중)
5. DispatcherServlcet 도착, multipart 가 아니므로 MultipartResolver 동작하지 않고 임시파일도 만들어지지 않음.
6. controller 호출 전 InputStream 파라미터에 바인딩(request.getInputStream()) 이 시점에도 본문은 아직 읽지 않은 상태
7. controller 호출 (본문은 아직 전송중 수 있음)
8. InputStream.read 호출시 tomcat 읽기 버퍼 또는 커널 수신 버퍼로부터 read
9. read 한 결과를 최종 저장 위치에 write (read() 가 -1 반환 때까지 read 반복)

```
raw body 는 임시 파일을 생성하지 않으므로 대용량 파일 받는 경우 부가적인 데이터가 없다면(파일과 더불어 별도 part로 구분 필요한 데이터) 성능상 유리할 수 있음
```

### raw body 참고

```
raw body 방식의 경우 수백mb 파일 api 에서 받는 경우 controller 이후 강제로
Thread.sleep 시 TCP 흐름제어에 의해 서버측 커널 수신 버퍼가 가득 찬 경우 client 측에서
더이상 데이터를 보내지 못하고 대기

만약 raw body 방식에서 inputStream 을 읽지 않고 응답한다고 해도 tcp 연결 재사용을 위해
요청은 서버로 보내지고 서버측에서는 maxSwallowSize 크기만큼 본문을 읽어서 버림
여기서 maxSwallowSize 을 초과하는 본문이 남아있는 경우 본문이 남아있는 상태에서 연결을 닫으므로
연결 재사용이 안 됨 (maxSwallowSize: 2mb)

반면 multipart 의 경우 controller 이후 강제 Thread.sleep 해도 이미 controller 가 
호출됐다면 임시파일 쓰기가 완료된 것이므로 client 측 파일 전송 요청에는 영향없음 (단, 당연히 응답에는 영향있음)

```

## reference

- https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-servlet/multipart.html
- https://docs.spring.io/spring-boot/api/java/org/springframework/boot/servlet/autoconfigure/MultipartProperties.html
- https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/arguments.html (InputStream)
- https://tomcat.apache.org/tomcat-10.1-doc/api/org/apache/tomcat/util/net/SocketProperties.html (appReadBufSize)