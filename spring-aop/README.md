

Spring aop cglib
- spring 3.2 > CGLIB 내장
- spring 4.0 > CGLIB 기본 생성자 필수 문제 해결
  - `objenesis` 기본 생성자 없이 객체 생성을 위한 라이브러리
- spring 4.0 > CGLIB 생성자 두 번 호출 문제 해결 (`objenesis`)
- spring boot 2.0 > CGLIB default
  - 구체 클래스 DI 가능
  - spring boot 는 AOP 적용시 default 로 CGLIB 사용 (`proxyTargetClass=true`)
