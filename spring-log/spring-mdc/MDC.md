MDC

MDC (Mapped Diagnostic Context)
- ThreadLocal을 활용하여 요청 Thread 마다 로그를 구분
  - ThreadLocal (Map) 에 해당 Thread 의 메타 데이터를 저장
- Thread 마다 (클라이언크 마다) 로그를 추적할 수 있음

적용
- spring 의 경우 앞 단의 Filter 를 등록하여 요청 앞 뒤로 요청을 구분할 수 있는 값을 세팅하고 초기화한다.
- `org.slf4j.MDC`
