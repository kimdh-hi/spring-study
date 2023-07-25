## JPA NamedLock

- MySQL (MariaDB) 를 이용한 분산락 구현

MySQL lock 함수
- `GET_LOCK(key, timeout)`
  - timeout 음수 입력시 무한대로 대기
  - 반환값 (1, 0, null)
  - `1`: 락 획득 성공
  - `0`: 락 획득 실패
  - `null`: 락 획득 중 에러
- `RELEASE_LOCK(key)`
  - 락 해제
  - 반환값 (1, 0, null)
- `IS_FREE_LOCK(key)`
  - 해당 key 로 락 획득 가능여부 확인
- `IS_USED_LOCK(key)`
  - 해당 key 로 락을 사용 중인지 확인

---
참고

https://techblog.woowahan.com/2631/