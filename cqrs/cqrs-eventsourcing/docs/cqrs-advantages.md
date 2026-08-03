# CQRS 장점

> 명령(쓰기)과 조회(읽기)의 모델을 분리했을 때 얻는 이점. 정의·트레이드오프는 [README](../README.md) 참고.

## 1. 읽기·쓰기 각각을 독립 최적화

- 문제: 하나의 모델(예: JPA 엔티티 하나)로 쓰기와 읽기를 다 처리하면, 쓰기에 좋은 구조와 읽기에 좋은 구조가 서로 충돌한다. 쓰기는 데이터 무결성을 위해 정규화(테이블을 잘게 나눔)해야 하고, 읽기는 화면 한 번에 뿌리려고 여러 테이블을 조인해야 하는데, 한 모델이 둘 다 만족시키려다 어느 쪽도 깔끔하지 못하게 된다.
- 쓰기 모델(Command): 데이터가 항상 규칙을 지키도록(불변식, invariant) 검증과 트랜잭션에 집중. 예를 들어 "재고는 음수가 될 수 없다", "주문 총액은 항목 합과 같아야 한다" 같은 규칙을 강제하는 데 최적화된 구조.
- 읽기 모델(Query): 검증 로직 없이 화면에 필요한 형태 그대로의 데이터만 보관. 조인·계산을 미리 끝내 둔 납작한(비정규화) 테이블이라 조회 코드가 단순해진다.
- 효과: 각 모델이 자기 목적에만 집중하므로 코드가 단순해지고, 한쪽 요구가 바뀌어도 다른 쪽을 건드릴 일이 줄어든다. — [Microsoft, "CQRS pattern"](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs) · [Martin Fowler, "CQRS"](https://martinfowler.com/bliki/CQRS.html)

## 2. 읽기·쓰기를 따로 확장(scale)

- 실제 서비스의 부하는 대부분 비대칭이다 — 글 하나를 쓰면 그 글을 수천 번 읽는다. 즉 읽기 트래픽이 쓰기보다 압도적으로 많다.
- 단일 모델이면 읽기 부하를 감당하려 서버 전체를 늘려야 해 쓰기 자원까지 같이 낭비된다. CQRS로 저장소를 물리적으로 나누면 **읽기 측만** 복제본(read replica)을 늘리거나 캐시를 붙여 부하가 몰리는 쪽만 정확히 보강할 수 있다.
- 저장 기술도 각 측에 맞게 다르게 고를 수 있다 — 쓰기는 트랜잭션이 강한 RDB, 읽기는 검색이 빠른 Elasticsearch나 조회가 빠른 문서 DB·Redis 캐시. 한 DB로 모든 요구를 억지로 맞출 필요가 없다. — [Microsoft](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)

## 3. 조회 성능

- 일반적인 CRUD에서는 조회할 때마다 여러 테이블을 조인하고 집계(COUNT, SUM 등)를 계산한다 — 화면이 복잡할수록 이 쿼리가 무거워진다.
- CQRS 읽기 모델은 이 결과를 **미리 계산해 저장**해 둔다. 예: 게시글 목록 화면에 필요한 제목·작성자명·댓글수·조회수를 한 행에 다 넣어 두면, 조회는 조인·집계 없이 그 테이블만 단순 SELECT 하면 끝난다.
- 화면·용도별로 읽기 모델을 여러 개 둘 수도 있다 — 목록용 요약 테이블, 상세용 테이블, 통계 리포트용 테이블을 각각 최적 형태로 유지. — [Microsoft](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)

## 4. 관심사 분리로 유지보수가 쉬워짐

- CQRS의 뿌리인 CQS(Command Query Separation): 모든 메서드는 상태를 바꾸는 **명령**이거나 값을 반환하는 **조회** 중 하나여야 하며 둘을 겸하지 않는다. 이걸 메서드 단위가 아니라 모델·객체 단위로 확장한 것이 CQRS다. — [Fowler, "CommandQuerySeparation"](https://martinfowler.com/bliki/CommandQuerySeparation.html)
- 효과: "이 코드가 데이터를 바꾸는가, 읽기만 하는가"가 구조적으로 명확해져 코드를 읽고 추론하기 쉽다. 조회 코드에는 부수효과가 없다고 신뢰할 수 있다.
- 변경 파급이 줄어든다 — 화면 요구가 바뀌어 읽기 모델을 고쳐도 쓰기 측 도메인 규칙은 그대로다. 반대로 도메인 규칙을 바꿔도 조회 쿼리는 영향받지 않는다.

## 5. 보안 · 권한 분리

- 쓰기 경로와 읽기 경로가 코드·모델 수준에서 나뉘어 있으므로, 데이터를 변경할 수 있는 통로를 명령 측에 한정하기 쉽다.
- 예: 대다수 사용자에게는 읽기 모델 조회만 열어 주고, 상태 변경은 인가된 명령 핸들러를 통해서만 가능하게 하면 변경 지점을 한곳으로 통제할 수 있다. — [Microsoft](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)

## 6. Event Sourcing · 태스크 기반 UI와의 궁합

- Event Sourcing(상태를 이벤트로 저장)과 결합하면 역할이 자연스럽게 갈린다 — 쓰기는 이벤트를 원천으로 저장하고, 읽기는 그 이벤트로 만든 프로젝션을 조회한다. 두 패턴은 별개지만 서로의 약점을 보완한다. — [event-sourcing-advantages](./event-sourcing-advantages.md)
- 단순 CRUD(생성·수정·삭제)가 아니라 "주문 취소", "배송 시작" 같은 **의도가 담긴 명령** 중심 UI와 잘 맞는다. 명령 하나가 곧 하나의 도메인 행위라서 사용자의 의도가 코드에 그대로 드러난다. — [Microsoft](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)

---

- 주의: 위 이점은 **도메인이 복잡하거나 읽기·쓰기 부하가 크게 비대칭**일 때 나온다. 단순 CRUD 화면에 적용하면 얻는 것 없이 모델이 둘로 늘어 관리 복잡성만 커진다. 서비스 전체가 아니라 이런 이점이 실제로 필요한 일부 영역에만 선택 적용을 권장. — [Fowler](https://martinfowler.com/bliki/CQRS.html)

## 참고 자료

- [Martin Fowler — CQRS](https://martinfowler.com/bliki/CQRS.html)
- [Martin Fowler — Command Query Separation](https://martinfowler.com/bliki/CommandQuerySeparation.html)
- [Microsoft — CQRS pattern](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)
