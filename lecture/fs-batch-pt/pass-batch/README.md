
### Tasklet - Repeat, RepeatStatus
Job -> Step -> Tasklet -> Chunk
- `CONTINUABLE`
  - Spring batch 에게 해당 Tasklet 을 다시 실행하도록 한다.
- `FINISHED`
  - 처리 성공 여부에 관계없이 Tasklet 을 완료처리하고 다음 처리를 진행

