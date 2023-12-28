## ShedLock

- sprign scheduler 는 여러 인스턴스에 대한 클러스터링 기능 제공하지 않는다.
  - 서버 인스턴스가 늘어난 상황에서 인스턴스마다 scheduler 가 중복 실행되는 것을 기본적으로 막지 못한다.
- `ShedLock` 은 scheduler 실행시 인스턴스 간 중복실행을 방지한다.


관련 라이브러리
- https://github.com/lukas-krecan/ShedLock