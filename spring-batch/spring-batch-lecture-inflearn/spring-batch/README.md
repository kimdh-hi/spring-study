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


