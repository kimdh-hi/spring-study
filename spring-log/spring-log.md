## spring log

slf4j - logback
- slf4j 는 Simple Logging Facade for Java 의 약자로 인터페이스이다.
- logback 은 slf4j 의 구현체 중 하나이다.
- logback 은 slf4j v1 을 fork 하여 개발된 버전으로 통상 log4j 로 불리는 log4j v2 와는 다르다.

logback 설정요소
- Logger
  - TRACE > DEBUG > INFO > WARN > ERROR
- Appender
  - 로그가 출력될 대상을 결정
  - consoleAppender, FileAppender, RollingFileAppender
```
RollingFileAppender
- FileAppender 와 동일하게 파일에 로그를 출력
- 특정 조건에 해당하는 경우 로그 출력 타겟 파일을 변경 (rolling policy)

rolling policy
- 시간을 기반으로 로그 타겟 파일 Rolling

<configuration>
  <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>logFile.log</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
      <!-- 하루 동안의 log -->
      <fileNamePattern>log.%d{yyyy-MM-dd}.log</fileNamePattern>

      <!-- 30일동안 -->
      <maxHistory>30</maxHistory>

      <!-- 최대 3gb -->
      <totalSizeCap>3GB</totalSizeCap>

    </rollingPolicy>
...
</configuration>
```
