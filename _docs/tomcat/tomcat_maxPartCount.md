## tomcat maxPartCount (FileCountLimitExceededException)

### Important: DoS in multipart upload
- https://www.cve.org/CVERecord?id=CVE-2025-48988
- multipart count 개수 제한이 없으므로 part 를 대량으로 포함하는 요청시 메모리 과다 사용 우려
- 제한없음(-1) —> 10개 제한
- 제한 초과시 FileCountLimitExceededException 에러

https://tomcat.apache.org/tomcat-10.1-doc/changelog.html?utm_source=chatgpt.com
- apache tomcat maxPartCount default 10 → 50 (10.1.43)

history
1. tomcat 10.1.42 maxPartCount  제한없음 → 10
2. springboot embed tomcat 10.1.41 → 10.1.42 upgrade (springboot 3.5.1)
2-1. multipart 요청시 part 10개 초과하는 경우 `FileCountLimitExceededException` 
2-2. maxPartCount 커스텀 프로퍼티 제공 `server.tomcat.max-part-count`
2-3. https://github.com/spring-projects/spring-boot/issues/45872
3. tomcat 10.1.43 (springboot  3.5.2)
3-1. maxPartCount 10 → 50