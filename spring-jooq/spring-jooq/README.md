spring jooq

spring boot 3.3 이하
- `org.springframework.boot:spring-boot-starter-jooq` 
  - `org.jooq:jooq` 최신버전 아님
  - exclude 후 최신버전 import

task
- jooq.generateSakilaDBJooq

---

generated dao

inheritance
- dao class 상속
- `org.jooq.Configuration` dao 추상 클래스 생성자로 전달 필요
- 불필요한 dao 클래스의 메서드까지 외부로 공개되는 단점이 있음
- composition 을 통한 generated dao 클래스 사용 권장
