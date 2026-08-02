## Spring Data Elasticsearch: 상품 검색 최소 샘플

- 원본은 H2 + JPA, 검색 인덱스만 Elasticsearch 로 구성
- 같은 키워드 대상 `LIKE` 검색과 ES 검색으로 차이 비교

### 언제 Elasticsearch 를 쓰는가

- 먼저 **안 쓰는 게 맞는 경우**를 걸러낸다. 

| 상황 | 판단 |
| --- | --- |
| `WHERE col = ?`, 범위, 정렬, 조인 | RDBMS 인덱스로 충분. ES 불필요 |
| 접두 검색 `LIKE '앞%'` | 일반 인덱스가 탄다. ES 불필요 |
| 데이터 수만 건 이하 + 검색 QPS 낮음 | MySQL 전문검색(`MATCH AGAINST`), PostgreSQL `tsvector` 로 충분 |
| 트랜잭션·정합성이 핵심인 데이터 | ES 는 원본 저장소로 부적합. 준실시간(NRT)이고 트랜잭션 없음 |

**ES 가 답이 되는 조건** — 아래 중 2개 이상이면 도입 검토

- 중간 일치 검색이 필수 — `LIKE '%키워드%'` 는 인덱스를 못 타고 full scan
- **관련도(score) 순 정렬**이 필요 — "제목에 있으면 위로" 같은 랭킹을 SQL 로 표현하기 어렵다
- 여러 필드를 한 번에 검색 — 제목·본문·태그·브랜드를 하나의 질의로
- 오타 보정(fuzzy), 동의어, 형태소 분석, 자동완성
- **집계(facet)** — 검색 결과에 대한 카테고리별/가격대별 개수를 매 질의마다
- 로그·이벤트처럼 append-only 대용량을 기간별 인덱스로 굴려야 함
- 읽기 부하를 원본 DB에서 떼어내야 함

정리하면 — **조건 일치(match)가 아니라 관련도(relevance)와 집계가 필요할 때** 쓴다.

### LIKE 로는 왜 안 되는가

```sql
select * from products where name like '%마우스 무선%'
```

- 인덱스 불가
- 문자열이 **그 순서 그대로** 들어있어야 매칭. "무선 마우스" 검색안됨
- 점수가 없으니 관련도 정렬 불가. 어떤 행이 더 적합한지 RDB는 모른다

Elasticsearch 는 색인 시점에 문서를 토큰으로 쪼개 **역색인(inverted index)** 을 만든다.

```
"무선 마우스"      → [무선, 마우스]
"게이밍 마우스"     → [게이밍, 마우스]
"...무선 충전 지원" → [무선, 충전, 지원]

역색인:  마우스 → [1, 2]     무선 → [1, 5]
```

질의어도 같은 방식으로 쪼개 토큰 단위로 매칭하므로 순서·위치와 무관하게 잡히고, 매칭 토큰 수와 희소성(BM25)으로 점수가 매겨진다.

### 실제 응답

`GET /products/search?q=무선` — 이름에 있는 문서가 점수 8배로 앞선다 (`name^3` 가중치)

```json
[
  { "name": "무선 마우스",  "score": 4.158,
    "highlights": ["<em>무선</em> 마우스", "저소음 블루투스 <em>무선</em> 마우스. 배터리 6개월"] },
  { "name": "USB 허브",    "score": 0.525, "highlights": ["<em>무선</em> 충전 지원"] },
  { "name": "기계식 키보드", "score": 0.495, "highlights": ["<em>무선</em> 블루투스 3대 동시 연결"] }
]
```

`q=무선 마우스` 로 두 방식 비교

| 방식 | 결과 |
| --- | --- |
| `LIKE '%무선 마우스%'` | 1건 (문자열이 통째로 들어있는 문서만) |
| ES `multi_match` | 4건 — 무선 마우스(6.78) > 게이밍 마우스(2.62) > USB 허브(0.52) > 기계식 키보드(0.49) |

`GET /products/facet?q=무선` — 검색 결과에 대한 카테고리 집계

```json
{ "주변기기": 2, "액세서리": 1 }
```

### 핵심 코드

- **인덱스 매핑을 명시 생성** — `indexOperations.createWithMapping()`. 자동 생성에 맡기면 `category` 가 `text` 로 잡혀 `terms` 집계가 실패한다. 집계·정렬 대상은 `FieldType.Keyword` 여야 한다
- **`multi_match` + boost** — `.fields("name^3", "description")` 로 이름 매칭에 3배 가중치
- **highlight** — 매칭 구간을 `<em>` 로 감싸 반환. 검색 UI의 기본
- **terms aggregation** — `withMaxResults(0)` 으로 문서는 안 받고 집계만 받는다


### 실무에서 추가로 봐야 할 것

- **이중 쓰기(dual write)** — 이 샘플은 `ProductService.register` 에서 DB 저장 후 ES 색인을 이어서 호출한다. ES 색인이 실패하면 검색 인덱스만 누락되고 롤백되지 않는다. 실제로는 트랜잭션 커밋 후 이벤트 발행(`@TransactionalEventListener(AFTER_COMMIT)`) → 재시도, 또는 Outbox / CDC(Debezium) 로 분리
- **한글 형태소 분석** — 기본 `standard` 분석기는 공백 단위로만 쪼갠다. "무선마우스"(붙여쓰기)는 안 잡힌다. `nori` 플러그인이 필요하다

  ```dockerfile
  FROM elasticsearch:9.4.2
  RUN elasticsearch-plugin install --batch analysis-nori
  ```
  이후 `@Setting` 으로 nori 분석기를 지정한다
- **준실시간(NRT)** — 색인 직후 바로 검색되지 않는다(기본 `refresh_interval` 1초). 테스트에서 `indexOperations.refresh()` 를 호출하는 이유. 운영 코드에서 매 쓰기마다 refresh 하면 성능이 무너진다
- **재색인(reindex)** — 매핑은 한 번 만들면 필드 타입 변경이 불가. alias 를 두고 새 인덱스로 재색인 후 alias 를 옮기는 방식이 표준
