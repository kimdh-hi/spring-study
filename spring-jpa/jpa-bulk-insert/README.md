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

---

### reference
- https://mariadb.com/docs/connectors/mariadb-connector-j
- https://techblog.woowahan.com/2695/
- https://jira.mariadb.org/browse/CONJ-1238
