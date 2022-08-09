
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