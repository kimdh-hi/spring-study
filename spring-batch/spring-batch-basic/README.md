
### Spring batch schema

- 배치 실행에 대한 메타 데이터를 저장

BATCH_JOB_INSTANCE
- Job 실행에 대한 최상위 테이블
- `job_name`, `job_key` 가 한 개 row를 식별

BATCH_JOB_EXECUTION
- job 의 시작/종료 시간, 상태 등에 대한 데이터

BATCH_JOB_EXECUTION_PARAMS
- Job 을 실행하기 위해 전달된 파라미터 정보

BATCH_JOB_EXECUTION_CONTEXT
- Job 이 실행되면서 공유되어야 할 데이터를 직렬화하여 저장

BATCH_STEP_EXECUTION
- Step 이 실행되는 동안 필요한 데이터
- Step 의 실행 결과

BATCH_STEP_EXECUTION_CONTEXT
- Step 이 실행되면서 공유되어야 할 데이터를 직렬화하여 저장

```
배치 메타 테이블 스키마 확인
spring-batch-core

org.springframework.batch.core.schema-{dbVendor}.sql
```

--- 

### Batch meta table mapping

JobInstance
- BATCH_JOB_INSTANCE 테이블과 매핑

JobExecution
- BATCH_JOB_EXECUTION 테이블과 매핑

JobParameters
- BATCH_JOB_EXECUTION_PARAMS 테이블과 매핑

ExecutionContext
- BATCH_JOB_EXECUTION_CONTEXT 테이블과 매핑

---

### Batch job에 parameter 적용

Program arguments: -chunkSize=20

- `JobParameters` 사용
```kotlin
val stepExecution = contribution.stepExecution
val jobParameters =  stepExecution.jobParameters
val chunkSize = jobParameters.getString("chunkSize", "10")!!.toInt()
```

- `@Value 어노테이션 + SpEL` 사용
  - `#{jobParameters[key]}`
  - `@JobScope` or `@StepScope` 등으로 반드시 스코프를 지정해줘야 한다
```kotlin
@Bean
fun itemsProcessingJob(): Job = jbf.get("itemsProcessingJob")
  .incrementer(RunIdIncrementer())
  .start(chunkBaseStep(null))
  .build()

// ... 생략

@Bean
@JobScope
fun chunkBaseStep(@Value("#{jobParameters[chunkSize]}") chunkSize: String?) {
// ... 생략
}
```

---

### Scope

`@JobScope`
- Job 실행 시점에 생성/소멸
- `Step` 에 선언가능
- `@Scope("job")` 과 동일

`@StepScope`
- Step 실행 시점에 생성/소멸
- `Tasklet`, `Chunk` (itemReader, itemProcessor, itemWrite)에 선언가능
- `@Scope("step")` 과 동일

각 Step과 Tasklet/Chunk에 Scope를 지정하면 Thread-safe 한 효과를 볼 수 있다.
- 여러 Step 이 동시에 한 개 Tasklet에 접근한다면?
  - Step Scope 를 지정하는 경우 Step에 의해 매번 새로운 Tasklet이 생성되므로 safe 하다.


