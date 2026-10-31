# JWT Key Rotation

- 스케줄러 통해 JWT 서명키 주기적으로 회전
  - 수동 회전 필요한 경우 별도 api 추가 (실적용시 해당 api 인가처리에 유의할 것)
- 회전된 키는 `jwt.ttl` 동안 검증용으로 유예 후 삭제
  - 회전 이전 발급 jwt도 만료 시까지 검증 위함

## 키 상태

| 상태 | 서명 | 검증 | 전이 |
|---|---|---|---|
| ACTIVE | O | O | 회전 시 생성 |
| VERIFY_ONLY | X | O | 회전 시 ACTIVE → VERIFY_ONLY, `demotedAt + jwt.ttl` 경과 후 삭제 |

## 스케줄

- 회전: `@Scheduled(cron = "${jwt.rotation-cron}")`
- 정리: 매시 정각, 유예 끝난 VERIFY_ONLY 키 삭제
