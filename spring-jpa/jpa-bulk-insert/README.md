## jpa batch insert

### mariadb
- mariadb connector/j 3.5.6 rewriteBatchStatements 재추가
  - https://jira.mariadb.org/browse/CONJ-1238 
  - 기존, rewriteBatchStatements 가 기본값 (false) 으로 설정된 경우에도 batch insert 수행 (useBatchMultiSend default true 옵션에 의한 것으로 추정, 관련 문서 못 찾음.)
  - mariadb connector/j 3.5.6 이후 rewriteBatchStatements 지정 필수
- ~~`rewriteBatchedStatements` 옵션 제거 (mariadb connector/j 3.0.3)~~
  - https://mariadb.com/docs/release-notes/connectors/java/3.0/3.0.3#option-removal

logging
- https://mariadb.com/docs/connectors/mariadb-connector-j/about-mariadb-connector-j#easy-to-use-logging

```
<?xml version="1.0" encoding="UTF-8"?>

<configuration>

  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
      <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
  </appender>

  <logger name="org.mariadb.jdbc" level="debug" additivity="false">
    <appender-ref ref="STDOUT"/>
  </logger>

  <root level="error">
    <appender-ref ref="STDOUT"/>
  </root>

</configuration>

```

### mysql
- rewriteBatchStatements (default: false)
- `jdbc:mysql://localhost/test?rewriteBatchedStatements=true`

logging
```
jdbc:mysql://localhost/test?rewriteBatchedStatements=true&createDatabaseIfNotExist=true&profileSQL=true&logger=Slf4JLogger&maxQuerySizeToLog=99999
```


### hibernate batch option
- https://docs.jboss.org/hibernate/orm/7.2/userguide/html_single/#batch
- jdbc.batch_size
  - 0 또는 음수 지정시 batch 비활성화
  - 한 번의 db 호출 내 포함시킬 수 있는 insert, delete, update statement 의 수
  - 크게 설정되는 경우 OOM 고려. 공식문서 예제에서는 100_000 을 예로 듬.
- order_inserts
  - default: false
  - flush 시점에 insert 문의 순서를 entity type(테이블), pk 로 재정렬하여 동일 테이블에 대한 쿼리가 연속적으로 실행되도록 한다.
  - insert 문이 테이블 단위로 정렬되는 경우 batch 처리가 최적화된다.
- order_updates
  - default: false
  - order_inserts 와 동일. batch 처리 최적화 위함.
  - order_updates 의 경우 deadlock 방생 가능성을 줄인다.



---

### reference
- https://mariadb.com/docs/connectors/mariadb-connector-j
- https://techblog.woowahan.com/2695/
- https://jira.mariadb.org/browse/CONJ-1238
- https://docs.jboss.org/hibernate/orm/7.2/userguide/html_single/#batch
