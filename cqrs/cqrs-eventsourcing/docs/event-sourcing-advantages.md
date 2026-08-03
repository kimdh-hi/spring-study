# Event Sourcing 장점

> 현재 상태를 직접 저장하는 대신, 상태를 바꾼 **사건(이벤트)들을 순서대로 쌓아** 저장하고 필요할 때 그것들을 재생해 현재 상태를 계산하는 방식. 정의·트레이드오프는 [README](../README.md) 참고.

## 0. 먼저: 무엇이 다른가

- 전통적 CRUD: 테이블에 "현재 상태"만 저장한다. 값을 수정하면 이전 값은 UPDATE로 덮여 사라진다. 예: 게시글 제목을 A→B→C로 바꾸면 테이블엔 C만 남는다.
- Event Sourcing: 상태를 직접 저장하지 않고 "무슨 일이 일어났는가"를 이벤트로 계속 추가(append-only)만 한다. 위 예라면 `ArticleWritten(A)`, `ArticleEdited(B)`, `ArticleEdited(C)` 세 이벤트가 순서대로 남는다. 현재 제목(C)은 이 이벤트들을 처음부터 재생(replay)해서 계산한다.
- 즉, 이벤트 스트림이 진짜 원천 데이터(source of truth)이고, 현재 상태는 거기서 파생되는 계산 결과다. — [Martin Fowler, "Event Sourcing"](https://martinfowler.com/eaaDev/EventSourcing.html)

## 1. 완전한 변경 이력 · 감사 로그

- 모든 상태 변경이 이벤트로 남고, 이벤트는 절대 수정·삭제하지 않고 추가만 하므로(append-only), 무엇이·언제·누가·어떤 순서로 바꿨는지가 자동으로 전부 기록된다.
- CRUD였다면 이력을 남기려고 별도 감사(audit) 테이블과 그것을 채우는 코드를 따로 만들어야 하는데, Event Sourcing에서는 이벤트 로그 자체가 곧 완전한 감사 기록이다.
- 이벤트는 "제목이 C로 바뀌었다" 같은 결과가 아니라 "사용자가 제목을 수정했다"는 비즈니스 사실이므로, 로그가 곧 업무 이력이 된다. — [Microsoft, "Event Sourcing"](https://learn.microsoft.com/en-us/azure/architecture/patterns/event-sourcing)

## 2. 과거 임의 시점의 상태 복원(시간 여행)

- 이벤트를 "특정 시점까지만" 재생하면 그 순간의 상태를 그대로 되살릴 수 있다. 예: 3번째 이벤트까지만 재생하면 그때의 게시글 상태가 나온다.
- 쓰임새: "지난주 화요일에 이 주문이 어떤 상태였나", "이 값이 잘못된 게 언제부터인가" 같은 질문에 정확히 답할 수 있다 — 디버깅·감사·분쟁 해결에 강력하다.
- CRUD는 현재 값만 남기므로 이런 과거 복원이 원천적으로 불가능하다. — [Fowler](https://martinfowler.com/eaaDev/EventSourcing.html)

## 3. 하나의 이벤트로 여러 읽기 모델을 자유롭게 생성·재구성

- 같은 이벤트 스트림을 각각 다르게 재생하면 용도가 다른 여러 읽기 모델(프로젝션)을 만들 수 있다 — 목록용, 통계용, 검색 색인용 등.
- 나중에 새 화면·새 리포트가 필요해지면, 처음부터 데이터를 새로 쌓을 필요 없이 **이미 저장된 과거 이벤트를 재생**해 새 읽기 모델을 만들면 된다. 즉 "과거 데이터가 없어서 못 만든다"는 상황이 줄어든다.
- 읽기 모델이 버그로 잘못 만들어졌거나 깨져도, 이벤트에서 다시 계산해 재구축하면 복구된다 — 읽기 모델은 원천이 아니라 언제든 다시 만들 수 있는 파생 데이터이기 때문이다. — [Fowler](https://martinfowler.com/eaaDev/EventSourcing.html)

## 4. 쓰기 성능 · 동시성

- 쓰기가 항상 이벤트를 새로 추가하는 INSERT뿐이다. 기존 행을 UPDATE하지 않으므로 같은 행을 두고 벌어지는 락 경합이 줄어, 쓰기 처리량 면에서 유리하다.
- append-only 구조라 저장이 단순하고, 순서가 보장된 로그라 이후 처리(프로젝션 갱신, 외부 발행)도 순차적으로 다루기 쉽다. — [Microsoft](https://learn.microsoft.com/en-us/azure/architecture/patterns/event-sourcing)

## 5. 시스템 통합 · 이벤트 기반 아키텍처

- 저장하는 이벤트를 그대로 다른 시스템에 발행(publish)할 수 있다. 예: `OrderPlaced` 이벤트를 저장하면서 동시에 결제·배송 시스템에 같은 이벤트를 전달.
- "DB에 저장하는 데이터"와 "다른 시스템에 알리는 메시지"가 동일한 하나의 이벤트라서, 저장 따로·발행 따로 하다 둘이 어긋나는 이중 기록 불일치 문제가 근본적으로 사라진다.
- CQRS와 결합하면 역할 분리가 자연스럽다 — 쓰기는 이벤트 저장(원천), 읽기는 프로젝션 조회. — [Microsoft](https://learn.microsoft.com/en-us/azure/architecture/patterns/event-sourcing) · [cqrs-advantages](./cqrs-advantages.md)

## 6. 디버깅 · 버그 복구

- 프로덕션에서 발생한 문제를, 그 시점까지의 이벤트를 그대로 재생해 개발 환경에서 재현할 수 있다 — "어떻게 이 상태가 됐는지"를 추측이 아니라 실제 사건 순서로 확인.
- 프로젝션(읽기 모델) 로직에 버그가 있었다면, 코드를 고친 뒤 이벤트를 다시 재생해 읽기 모델을 새로 만들면 된다. 원천인 이벤트는 불변이라 이 과정에서 데이터가 손실되지 않는다.

---

- 주의: 위 이점의 대가로 다음을 감수해야 한다 — 읽기 모델이 쓰기보다 약간 늦게 갱신되는 최종적 일관성(eventual consistency), 시간이 지나며 바뀌는 이벤트 구조의 버전 관리, 이벤트가 수만 개로 길어졌을 때 매번 전부 재생하는 비용(→ 중간 상태를 저장하는 스냅샷 필요). 그래서 감사·이력·상태 재구성이 실제로 가치 있는 도메인에 선택 적용을 권장. — [Microsoft](https://learn.microsoft.com/en-us/azure/architecture/patterns/event-sourcing)

## 참고 자료

- [Martin Fowler — Event Sourcing](https://martinfowler.com/eaaDev/EventSourcing.html)
- [Microsoft — Event Sourcing pattern](https://learn.microsoft.com/en-us/azure/architecture/patterns/event-sourcing)
