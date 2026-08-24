# 헥사고날 아키텍처 (Ports and Adapters)

## 정의와 배경

- 비즈니스 로직을 중앙에 두고, 외부 세계(웹·DB·메시지·외부 API)와의 접점을 **코어가 정의한 인터페이스(포트)** 로만 연결하는 구조
- Alistair Cockburn, 2005. 원래 이름은 *Ports and Adapters* — "육각형"은 다이어그램 편의를 위한 은유
- 원문 의도: "애플리케이션이 사용자·프로그램·자동화 테스트·배치에 의해 동등하게 구동되고, 최종 런타임 장치와 DB 없이도 개발·테스트될 수 있게 한다"
- 해결하려는 문제: 레이어드는 `presentation → business → data access` 로 의존하므로 **DB 스키마 변경이 비즈니스 계층까지 전파**됨

## 구성 요소

- **코어** — 도메인 + 유스케이스. 프레임워크·DB·HTTP를 모름
- **포트** — 코어가 선언한 인터페이스
  - 인바운드(드라이빙): 외부가 코어를 호출하는 입구 = 유스케이스
  - 아웃바운드(드리븐): 코어가 외부를 호출하는 출구 = 저장소·외부 API
- **어댑터** — 포트를 특정 기술로 구현/호출하는 변환기
  - 프라이머리: REST 컨트롤러, 컨슈머, 배치, CLI
  - 세컨더리: JPA 리포지토리, Redis 클라이언트, HTTP 클라이언트

## 핵심 규칙 3개

1. **의존 방향은 항상 안쪽** — 어댑터 → 포트 → 코어. 코어에서 밖으로 나가는 컴파일 의존 없음
2. **포트는 코어가 정의** — 어댑터가 인터페이스를 주면 그건 레이어드. 의존성 역전(DIP)이 실제 메커니즘
3. **변환은 어댑터의 일** — 외부 DTO·엔티티·JSON을 도메인 모델로 바꾸는 책임은 어댑터

## 흐름 (이 예제 기준)

```
[REST]  ──▶ PostController ──▶ PostUseCase(포트) ──▶ PostService ──▶ Post(도메인)
[배치]  ──▶      (미구현)  ─┘                            │
                                                          ▼
                                             PostRepository(포트)
                                                          ▲
                                        PostPersistenceAdapter ──▶ JPA / H2
```

## 왜 사용하나

- **입구 추가가 싸다** — LINE: HTTP API + WebSocket 동시 지원에 업무 로직 수정 거의 없었음
- **데이터 출처 교체가 싸다** — Netflix: 모놀리스 읽기 제약에 걸려 특정 읽기를 GraphQL 마이크로서비스로 이전, 비즈니스 로직 무변경. 카카오스타일: Redis → Valkey 교체에 도메인 무수정
- **테스트가 빠르다** — 아웃바운드 포트를 Fake 로 교체해 인프라 없이 규칙 검증 (`PostServiceTest` 는 스프링 컨텍스트 없이 실행)
- **배포 단위를 쪼갤 수 있다** — 카카오뱅크: 코어를 모듈로 분리해 api 만 DB 어댑터를 의존, 커넥션 풀 제약 해소
- **도메인 규칙이 한곳에 모인다** — 권한·불변식이 컨트롤러나 쿼리로 새지 않음

## 흔한 오해

- **포트가 6개** — 원저자는 2~4개를 선호하고, 개수를 잘못 골라도 특별한 손해는 없다고 씀
- **레이어드의 변종** — 계층 수가 아니라 의존 방향이 본질. 코어 내부 구조는 열려 있음
- **DB 교체용 구조** — DB 벤더 교체는 드묾. 실효는 데이터 출처 교체·입구 추가·테스트·배포 분리에서 나옴
- **인터페이스를 다 뽑아야 함** — 인바운드 포트는 입구가 2개 이상일 때 값이 생김. 구현 하나뿐인 인터페이스는 과설계 지적 1순위
- **DDD 필수** — 독립 개념. 애그리거트와 결합이 좋아 함께 쓰는 사례가 많을 뿐

## 어니언 / 클린과의 관계

- 같은 원리(로직 중앙 + 인프라 주변 + 의존성 역전)의 세 갈래
- **헥사고날**(2005, Cockburn): 포트/어댑터라는 연결 메커니즘을 명시, 코어 내부는 규정 안 함
- **어니언**(2008, Palermo): 동심원으로 코어 내부를 세분화, 의존 방향 달성 수단은 미명시
- **클린**(2012, Martin): 둘을 종합해 엔티티/유스케이스/인터페이스 어댑터와 의존성 규칙을 규범화
- 이름보다 "무엇을 코어로 보호하는가"가 실제 차이

## 얼마나 순수하게 갈 것인가

- 순수성은 0/1 이 아니라 아래 5개 축에서 각각 고르는 선택

| # | 지점 | 순수한 쪽 | 실용적인 쪽 |
|---|---|---|---|
| 1 | 도메인 vs 영속성 모델 | `Post` / `PostJpaEntity` 분리 + 매핑 | JPA 엔티티를 도메인으로 사용 |
| 2 | 인바운드 포트 | `PostUseCase` 인터페이스 | 컨트롤러가 `PostService` 직접 주입 |
| 3 | 유스케이스 입력 | 유스케이스별 Command 객체 | 원시 파라미터 / 웹 DTO 재사용 |
| 4 | 조회 경로 | 조회도 포트 → 도메인 → 응답 | 어댑터에서 조회 DTO 직행 (CQRS-lite) |
| 5 | 저장 단위 | 애그리거트 통째 로드/저장 | 부분 업데이트 쿼리 허용 |

- Tom Hombergs 의 매핑 3전략(no / two-way / full)은 1·3번 축의 조합에 붙인 이름

### 판단 기준 (2개 이상 예 → 순수하게)

1. 도메인에 진짜 규칙이 있는가 (분기·불변식·권한). CRUD 면 아니다
2. 같은 유스케이스를 부르는 입구가 2개 이상인가 (REST + 배치 + 컨슈머)
3. 나갈 곳이 바뀔 가능성이 있는가 — **DB 벤더가 아니라 "데이터 출처" 기준으로 볼 것**
4. 인프라 없이 도메인 규칙을 테스트할 필요가 큰가
5. 코어를 별도 배포 단위로 떼어낼 계획이 있는가

### 이 예제의 선택과 비용

- **순수하게 간 축**: 1(도메인/엔티티 분리), 2(인바운드 포트) — 구조를 보여주는 예제 목적상 유지
- **포기한 축**
  - 3: Command 없이 원시 파라미터. 파라미터가 5개를 넘으면 승격
  - 4: 조회도 도메인 경유. 목록 성능이 문제되면 `PostQueryPort` + 조회 DTO 분리
  - 5: 애그리거트 통째 저장. 댓글이 많아지면 `CommentRepository` 포트 분리
- **실제로 낸 비용**
  - `toEntity()` / `toDomain()` 매핑 코드가 필드 수만큼 증가
  - 불변 `Post` 를 반환하므로 어댑터가 `save(merge)` 로 전체를 덮어씀 — 댓글 1개 추가에도 컬렉션 diff 발생, 삭제는 `orphanRemoval` 이 담당
  - `Comment(id = null)` 을 도메인이 만들고 DB 가 id 를 채움 → 저장 전/후 상태가 다른 두 종류의 도메인 객체
  - `comments` EAGER 매핑으로 목록 API 가 모든 댓글을 로드
  - `PostUseCase` / `AuthorUseCase` 는 구현이 하나뿐

## 언제 쓰지 말까

- 단순 CRUD — Tom Hombergs 본인 경고: "CRUD 애플리케이션이면 이런 아키텍처는 아마 오버헤드"
- 도메인 복잡도보다 기술 복잡도가 지배적인 시스템 (배치 파이프라인, 단순 프록시)
- 수명이 짧거나 스펙이 곧 뒤집힐 프로젝트
- 경계를 지킬 팀 합의가 없을 때 — 지켜지지 않는 경계는 매핑 비용만 남음
- Victor Rentea 의 인터페이스 존재 기준: 구현이 둘 이상 / 내부 링을 보호하는 의존성 역전 / 클라이언트 라이브러리로 배포 — 셋 중 하나여야 인터페이스가 존재할 가치가 있음

## 실무 이식 순서

1. 의존성 방향만 먼저 잡는다 (도메인은 프레임워크를 모른다) — 투자 대비 회수 최대 구간
2. 아웃바운드 포트를 코어 쪽에 둔다. 인바운드 포트는 입구가 2개가 될 때 도입
3. 매핑은 two-way 부터. Command 객체는 검증이 유스케이스별로 갈릴 때
4. 조회는 처음부터 별도 경로로 빼도 된다 — 위반이 아니라 표준적 절제
5. 경계가 무너지면 ArchUnit → 그다음 멀티모듈 (카카오뱅크 사례)

## 참고 자료

- [Alistair Cockburn — Hexagonal Architecture (2005, 원전)](https://alistair.cockburn.us/hexagonal-architecture/)
- [Netflix TechBlog — Ready for changes with Hexagonal Architecture](https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749)
- [카카오뱅크 — 유일한 멀티모듈 헥사고날 아키텍처: 메시지 허브 적용기](https://tech.kakaobank.com/posts/2311-hexagonal-architecture-in-messaging-hub/)
- [카카오스타일 — 코드 사례로 보는 Domain-Driven 헥사고날 아키텍처](https://devblog.kakaostyle.com/ko/2025-03-21-1-domain-driven-hexagonal-architecture-by-example/)
- [LINE — 지속 가능한 소프트웨어 설계 패턴: 포트와 어댑터 아키텍처](https://engineering.linecorp.com/ko/blog/port-and-adapter-architecture/)
- [AWS Prescriptive Guidance — Hexagonal architectures](https://docs.aws.amazon.com/prescriptive-guidance/latest/hexagonal-architectures/overview.html)
- [Victor Rentea — Overengineering in Onion/Hexagonal Architectures](https://victorrentea.ro/blog/overengineering-in-onion-hexagonal-architectures/)
- [Tom Hombergs — Hexagonal Architecture with Java and Spring](https://reflectoring.io/spring-hexagonal/) / [buckpal 예제](https://github.com/thombergs/buckpal)
- [꿈공장 — 헥사고날 아키텍처, 어디까지 적용해야 할까](https://yearnlune.github.io/general/hexagonal-architecture)
