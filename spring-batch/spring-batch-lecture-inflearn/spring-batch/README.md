## Spring batch

@EnableBatchProcessing
- 4개 설정 클래스 실행
  - `BatchAutoConfiguration`
    - `JobLauncherApplicationRunner` 실행
  - `SimpleBatchConfiguration`
    - `JobBuilderFactory`, `StepBuilderFactory` 생성
  - `BatchConfigurerConfiguration`
    - `BasicBatchConfigurer`
    - `JpaBatchConfigurer`
    - `CustomBatchConfigurer` (사용자 정의)
---

spring batch 5

JobBuilderFactory, StepBuilderFactory deprecated
```kotlin
@Configuration
class HelloJobConfiguration(
  private val jobRepository: JobRepository,
  private val transactionManager: PlatformTransactionManager,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun helloJob() = JobBuilder("helloJob", jobRepository)
    .start(helloStep1())
    .next(helloStep2())
    .build()

  @Bean
  fun helloStep1() = StepBuilder("helloStep1", jobRepository)
    .tasklet(
      { _, _ ->
        log.info("helloStep1...")
        RepeatStatus.FINISHED
      }, transactionManager
    )
    .build()

  @Bean
  fun helloStep2() = StepBuilder("helloStep2", jobRepository)
    .tasklet(
      { _, _ ->
        log.info("helloStep2...")
        RepeatStatus.FINISHED
      }, transactionManager
    )
    .build()
}
```

@EnableBatchProcessing
- spring batch 4.x 버전까지 자동설정을 위한 필수 어노테이션이었음
- spring batch 5.x 부터 `BatchAutoConfiguration` 에 `@ConditionalOnMissingBean` 으로 등록되어 사용하지 않아도 됨 
- batch 설정 커스텀이 필요한 경우 `DefaultBatchConfiguration` 클래스를 상속해서 사용한다.

---

Spring batch meta table
- 위치: spring-batch-core.org/springframework/batch/core/schema-*.sql 
- meta table 생성 옵션 (`spring.batch.jdbc.initialize-schema`)
  - ALWAYS: 항상 스키마 생성 스크립트 실행
  - EMBEDDED: 내장 DB 의 경우에만 스크립트 실행
  - NEVER: 스크립트 생성x

BATCH_JOB_INSTANCE
- Job 실행시 JobInstance 정보 저장
- job_name, job_key 을 key 로 사용

BATCH_JOB_EXECUTION
- Job 의 실행정보 저장
- job 생성, 시작, 종료 시간, 실행상태 등 저장

BATCH_JOB_EXECUTION_PARAMS
- JobParameter 저장

BATCH_JOB_EXECUTION_CONTEXT
- Job 실행간 상태정보, 공유 데이터를 json 포맷으로 저장
- Step 간 공유 가능

BATCH_STEP_EXECUTION
- Step 의 실행정보 저장
- Step 생성, 시작, 종료 시간, 실행상태 등 저장

BATCH_STEP_EXECUTION_CONTEXT
- Step 실행간 상태정보, 공유 데이터를 json 포맷으로 저장
- Step 별로 저장되고 Step 간 공유 불가

---

JobInstance
- Job 실행시 생성되는 Job 의 논리적 실행단위 객체
- 최초 Job + JobParameter 로 JobInstance 생성
- 이전 동일한 Job + JobParameter 로 실행하는 경우 기존 JobInstance 리턴 (재사용)
  - DB로 부터 JobName + JobKey(JobParameter 해시값) 으로 JobInstance 롤 찾음 (중복 불가)
  - `BATCH_JOB_INSTANCE` 테이블에 저장
