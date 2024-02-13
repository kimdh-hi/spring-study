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

### JobInstance
- Job 실행시 생성되는 Job 의 논리적 실행단위 객체
- 최초 Job + JobParameter 로 JobInstance 생성
- 이전 동일한 Job + JobParameter 로 실행하는 경우 기존 JobInstance 리턴 (재사용)
  - DB로 부터 JobName + JobKey(JobParameter 해시값) 으로 JobInstance 롤 찾음 (중복 불가)
  - `BATCH_JOB_INSTANCE` 테이블에 저장

```
JobInstanceAlreadyCompleteException: A job instance already exists and is complete for identifying parameters
- 동일 jobName, jobParameters 로 실행된 jobInstance 가 있는 경우 Job 실행 불가
```

### JobParameters
- Job 실행시 포함된 파라미터 정보를 가지는 객체
- 한 개 Job 에 여러 개 존재하는 JobInstance 를 구분
```
JobParameters 생성, 바인딩
- application 실행시 주입
  - Java -jar xxx.jar name=kim requestDate(date)=2024/01/01 num(long)=2 num2(double)=1.1
- 코드로 생성
  - JobParameterBuilder, DefaultJobParametersConverter
- SpEL
  - @Value("#{jobParameter[requestDate]}") + @JobScope, @StepScope
```
- ParameterType
  - STRING, DATE, LONG, DOUBLE
```kotlin
// step 내 jobParameters 접근 (반환타입 확인)

@Bean
fun step1() = StepBuilder("step1", jobRepository)
  .tasklet(
    { contribution, chunkContext ->
      log.info("step1...")

      val jobParameters: JobParameters = contribution.stepExecution.jobExecution.jobParameters
      val jobParameters2: JobParameters = contribution.stepExecution.jobParameters
      val jobParameters3: Map<String, Any> = chunkContext.stepContext.jobParameters

      RepeatStatus.FINISHED
    }, transactionManager
  )
  .build()
```

### JobExecution
- JobInstance 의 한번의 시도를 의미하는 객체
- Job 실행중 발생한 모든 정보 저장
  - 시작/종료 시간, 상태(시작됨, 완료, 실패), 종료상태
- JobExecution 은 `FAILED`, `COMPLETED` 등 Job 의 실행 결과 상태를 가짐
- JobExecution 의 실행 상태가 `COMPLETED` 인 경우 JobInstance 실행이 완료된  것이므로 `재실행 불가`
- JobExecution 의 실행 상태가 `FAILED` 인 경우 JobInstance 실행이 완료되지 않은 것 이므로 `재실행 가능`
  - `JobParameter` 가 동일한 Job 즉, 동일한 JobInstance 도 계속 실행 가능
- JobExecution 의 상태가 `COMPLETED` 가 될 때까지 하나의 JobInstance 가 여러 번 실행될 수 있음
- JobExecution 데이터는 `BATCH_JOB_EXECUTION` 테이블과 매핑

---

### validator
- Job 실행에 필요한 파라미터 검증
- `DefaultJobParametersValidator` 제공
  - `requiredKeys`, `optionalKeys` (필수값, 선택값에 대한 검증)
  - `AbstractJob` 참고
- 복잡한 validator 가 필요한 경우 커스텀 가능 (인터페이스 구현)
  - `JobParametersValidator`
- jobParameter 전달 테스트
  - Program arguments: --job.name=job name=kim

### preventRestart
- job 재시작 여부
- default=true (restartable=true)
- false 설정시 job 재시작 불가
  - job 재시작 (failed 인 job 도) 시도시 `JobRestartException` 발생
  - job 성공/실패 여부에 관계없이 재시작 여부를 판단
- 재시작 시 관련 기능으로 최초 job 실행과는 관계없는 옵션
  - `JobExecution` 이 존재하지 않는 경우 옵션과 관계없이 job 실행
  - `JobExecution` 이 존재하는 경우 `JobRestartException` 발생 

### incrementer
- JobParameters 의 값을 증가시켜서 새로운 JobParameters 를 반환
- 기존 JobParameters 변경없이 동일 Job 을 여러 번 실행하려하는 경우 사용
- `RunIdIncrementer` 재공
  - `JobParametersIncrementor` 인터페이스 통한 커스텀 가능

---

### tasklet
- step 내에 구성되는 객체로 주로 단일 테스크를 수행한다.
- RepeatStatus
  - `FINISHED`: tasklet 종료, `RepeatStatus` null 반환시 `FINISHED`로 처리된다.
  - `CONTINUABLE`: tasklet 을 반복실행한다.
    - `FINISHED` 혹은 예외가 발생할 때까지 tasklet 을 반복실행

### step startLimit
- step 실행 횟수 제한
- 설정값을 초과해서 다시 실행 시도시 `StartLimitExceededException` 발생
  - default: `Integer.MAX_VALUE`

### allowStartIfComplete()
- 재시작 가능 job 내 step 에서 해당 step 의 이전 성공 여부와 관계없이 항상 step 을 실행
  - 기본적으로 COMPLETE 상태의 step 은 job 재시작시 스킵된다.
  - default: false
- `allow-start-if-complete` 가 true 로 설정된 step 은 항상 실행된다.

```kotlin
  @Bean
  fun step1() = StepBuilder("step1", jobRepository)
    .tasklet(
      { _, _ ->
        log.info("step1...")
        RepeatStatus.FINISHED
      }, transactionManager
    )
    .startLimit(3)
    .allowStartIfComplete(false)
    .build()
```

