
```text
gradle clean build
java -jar spring-batch-basic-0.0.1-SNAPSHOT.jar --spring.batch.job.names=jobName
```

### 배치 실행 방식

OS 스케줄러
- 리눅스 crontab ...

Quartz Scheduler 
- Quartz + SpringBatch
- 애플리케이션 내부에서 스케줄링이 가능하므로 batch 실행이 OS 스케줄러에 비해 빠르다

Jenkins (👍)
- 마스터에서 슬레이브로 전달되는 명령으로 배치 프로그램이 동작한다.

---

### Batch 병렬처리

Multi-threaded step
- 싱글 프로세스 내에서 멀티 쓰레드 활용 

Parallel step
- 싱글 프로세스에서 멀티 쓰레드 활용

Remote chunking
- 멀티 프로세스 활용 (여러 개 서버)

Partitioning
- step을 파티셔닝한 후 여러 쓰레드에서 처리되도록 or 멀티 프로세스 활용

