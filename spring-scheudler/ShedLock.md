## ShedLock

- sprign scheduler 는 여러 인스턴스에 대한 클러스터링 기능 제공하지 않는다.
  - 서버 인스턴스가 늘어난 상황에서 인스턴스마다 scheduler 가 중복 실행되는 것을 기본적으로 막지 못한다.
- `ShedLock` 은 scheduler 실행시 인스턴스 간 중복실행을 방지한다.


속성
- name(*)
  - lock 이름
  - 동일 name 의 lock 은 shedlock 테이블의 동일 레코드를 사용
- lockAtMostFor(*)
  - lock이 최대로 유지되는 시간
  - 지정한 시간 이후 lock 해제
  - 반드시 예상 작업시간보다 크게 설정
  - 일반적인 경우 job 종료시 lock 은 해제된다.
  - `lockAtMostFor=9m`
- lockAtLeastFor
  - lock이 최소로 유지되는 시간
  - lock 획득 후 최소 지정한 시간동안은 lock 이 해제되지 않는다.


관련 라이브러리
- https://github.com/lukas-krecan/ShedLock