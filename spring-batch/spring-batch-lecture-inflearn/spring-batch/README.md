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

---

### TaskletStep
- Job -> Step -> `RepeatTemplate` -> `Tasklet` (loop)...
  - Tasklet 의 business logic 결과
    - exception: loop 종료, step 종료
    - RepeatStatus CONTINUABLE: RepeatTemplate에 의해 tasklet 반복
    - RepeatStatus FINISHED: loop 종료, step 종료

### JobStep
- Step 이 외부의 Job을 포함하는 것
- 포함된 Job 이 실패하면 해당 Step이 실패하므로 상위 기본 Job 도 실패한다
- 모든 메타 데이터는 기본 Job 과 외부 Job 각각 저장된다.

---

### Error
<br/>

```
Caused by: java.lang.IllegalArgumentException: Job name must be specified in case of multiple jobs
```
- 여러 개 job 이 있는 경우 job name 을 명시해야 함
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#multiple-batch-jobs
```
Multiple Batch Jobs
Running multiple batch jobs is no longer supported. If the auto-configuration detects a single job is, it will be executed on startup. If multiple jobs are found in the context, a job name to execute on startup must be supplied by the user using the spring.batch.job.name property.
```
- 복수 개 job 이 정의된 경우 실행시점에 어떤 job 을 실행할 것이지 프로퍼티로 명시해줘야 한다.
  - `spring.batch.job.name`

---

### FlowJob
- step 을 순차적으로만 구성하지 않는 방식
  - step 이 실패하더라도 job 은 실패로 끝나지 않도록 해야 하는 경우
  - step 성공시 다음 실행할 step 을 구분해서 실행해야 하는 경우 
  - 특정 step 이 실행되지 않도록 해야 하는 경우

Transition
- Flow 내 step 의 조건부 전환을 정의
- Job 정의시 `on(pattern)` 를 호출허면 `TransitionBuilder` 가 반환되어 Transition flow 를 구성할 수 있다.
- Step 의 종료상태가 `pattern` 과 일치하는 게 없는 경우 해당 job 은 예외를 발생하고 실패한다.
```
on
- Step 의 실행결과 ExitStatus 와 매칭
- pattern 과 ExitStatus 가 매칭되는 경우 다음 실행할 step 을 지정한다.
- 허용 특수문자
  - * : 0개 이상의 모든 문자와 매칭 (모든 ExitStatus 와 매칭)
  - ? : 1개 문자와 매칭
  - ex) a*d, a?c
```

```kotlin
/**
 * step1 "FAILED" 시 step2 실행
 *   step2 "FAILED` 시 stop
 * step1 가 "FAILED" 가 아닌 경우 step3 실행
 * step2 가 "FAILED" 가 아닌 경우 step5 실행
 */

  @Bean
  fun job() = JobBuilder("job", jobRepository)
    .start(step1())
      .on("FAILED")
      .to(step2())
      .on("FAILED")
      .stop()
    .from(step1())
      .on("*")
      .to(step3())
      .next(step4())
    .from(step2())
      .on("*")
      .to(step5())
      .end()
    .build()
```

---

### @JobScope, @StepScope
- 위 어노테이션으로 @Bean 생성시 빈 실행시점에 빈 객체 초기화를 미룬다. (lazy binding)
  - 빈 실행시점으로 초기화를 미뤘기 때문에 `@Value` 를 이용하여 step, tasklet 등에서 특정 파라미터 등을 참조할 수 있다.
  - `@Value` 사용시 해당 bean 은 반드시 @JobScope 또는 @StepScope 가 있어야 한다.
- `@JobScope`
  - Step 에 선언 가능
  - `@Value`: JobParameters, jobExecutionContext 참조 가능
- `@StepScope`
  - Tasklet, ItemReader/writer/processor 에 선언 가능
  - `@Value`: JobParameters, JobExecutionContext, stepExecutionContext 참조 가능

---

### FlatFileItemReader

- DelimitedLineTokenizer
  - 한 개 라인을 특정 구분자로 구분하여 토큰화하는 방식
  - defualt: `, 콤마`
  - 한 개 라인을 구분자를 기준으로 토큰화하여 `FieldSet` 반환 
```kotlin
  @Bean
  fun itemReader() = FlatFileItemReaderBuilder<TestCsv>()
    .name("flatFile")
    .resource(ClassPathResource("/test.csv"))
    .fieldSetMapper(TestCvsFieldSetMapperV2())
    .linesToSkip(1)
    .delimited().delimiter(",")
    .names("id", "name", "age")
    .build()

class TestCvsFieldSetMapperV2: FieldSetMapper<TestCsv> {
  override fun mapFieldSet(fieldSet: FieldSet): TestCsv {
    return TestCsv(
      fieldSet.readString("id"),
      fieldSet.readString("name"),
      fieldSet.readInt("age")
    )
  }
}
```
  - `names` 지정시 `fieldSetMapper` 에서 인덱스가 아닌 지정한 이름으로 참조 가능 


- FixedLengthTokenizer
  - 한 개 라인을 설정한 고정길이를 기준으로 토큰화하는 방식
  - 범위 지정
```kotlin
    .addColumns(Range(1, 5))
    .addColumns(Range(6, 9))
    .addColumns(Range(10, 11))
```

### Exception Handling
- 토큰화시 parcing 에러 처리
  - IncorrectTokenCountException (DelimitedLineTokenizer)
  - IncorrectLineLengthException (FixedLengthTokenizer)
- `strict=false` 설정시 검증 x
  - `default=true`

---