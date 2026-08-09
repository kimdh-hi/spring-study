# hierarchical-query

계층구조 db 데이터 조회시 이슈 및 개선방안 검토

---

## 1. 이슈
- parent-child 구조에서 A 이하 B 10개, B이하 C 10개, C 이하 D 10개인 경우 전체 목록조회시 별도 개선없이 조회하는 경우 이슈

```
A: 1회 => 1
B: 1*10회 => 10
C: 10*10회 => 100
D: 100*10회 => 1000

총 1,111회
```

---

## 2. 기술적 고민 전 기획적으로 풀 수 있는지 확인
- 모든 depth 의 부모, 자식 tree 가 모두 조회되어야 하는 것이 맞는지?
- 최상위 부모 목록만 노출하고, 각각 부무 id 를 받아서 그 이하 자식 목록만 조회하는 ui가 허용된다면 Materialized Path, CTE 등 개선안 검토할 필요없음
- 계층 구조 ui 에 모든 요소 출력이 필요한지, lazy loading UI 인지를 확실히 결정하고 기술적인 고민 필요

---

## 3. 계층 구조 데이터 조회 방법

### 3-1. 개선X

- `parent_id` 만 가진 구조에서 자식을 얻으려면 노드마다 `where parent_id = ?` 를 반복 → 쿼리 수가 노드 수에 비례

```sql
select ... from tree where id = ?           -- 루트 1번
select ... from tree where parent_id = ?    -- 노드마다 1번씩, 노드 수만큼 반복
```

### 3-2. 레벨 단위 IN 조회 + 메모리 조립

- 조회 대상이 될 root id 로 1회 조회
- 조회 결과를 parent 로 하는 child 목록을 1회 조회
- parent 가 있는 child 가 없을 때까지 반복

```kotlin
var parentIds = listOf(root.id)
while (parentIds.isNotEmpty()) {
  val children = treeRepository.findAllByParentIdIn(parentIds)
  nodes += children
  parentIds = children.map { it.id }
}
```

```sql
select ... from tree where id = ?                          -- 시작 노드 1번
select ... from tree where parent_id in (?)                -- 레벨 1
select ... from tree where parent_id in (?, ?)             -- 레벨 2
select ... from tree where parent_id in (?, ?, ?, ?)       -- 레벨 3
select ... from tree where parent_id in (...)              -- 레벨 4, 결과 0 → 종료
```

- 1번 이슈의 1,111회 → **깊이 + 2 = 6회**. 노드가 10배여도 6회

- **장점**
  - 스키마 변경 없음
  - 서브 트리만 조회
  - `parent_id` 인덱스만 필요
  - 인접 리스트로 가능한 최선
- **단점**
  - 깊이만큼 왕복
  - 마지막 레벨은 항상 빈 결과 1회 낭비
- **주의사항**
  - `in` 파라미터가 레벨 폭만큼 증가
  - 한 레벨이 수천 개면 청크 분할 조회

### 3-3. Materialized Path (mpath)

- 각 노드가 루트부터 **자기 부모까지**의 id 를 이어붙인 문자열을 `path` 컬럼에 별도 저장
- `path + 자기 id + "/"` 를 prefix 로 `startsWith` 검색 1회 → 자식·손자 이하 전부 조회
- 계층 구조는 메모리에서 조립(3-2 와 동일)

```
r     /                 -- 실제로는 id. 읽기 쉽게 이름으로 표기
a     /r/
a1    /r/a/
a1x   /r/a/a1/
```

**조회** — 자손만 반환되므로 이미 로드한 시작 노드를 앞에 결합

```kotlin
val descendants = query.selectFrom(tree)
  .where(tree.path.startsWith(node.path + node.id + "/"))
  .fetch()

TreeView.assemble(listOf(node) + descendants)
```

**저장** — 생성 시점에 `부모 path + 부모 id`, 부모가 없으면 `/`. 자기 id 불필요 → id 생성 전략 자유, 추가 조회 없이 INSERT 1회

```kotlin
path = parent?.let { it.path + it.id + "/" } ?: "/"
```

**갱신(이동)** — 자기 `path` 는 새 부모 기준으로 재생성, 자손 전체는 prefix 치환 bulk UPDATE 1회

```kotlin
node.moveTo(newParent)

query.update(tree)
  .set(tree.path, Expressions.asString(newPrefix).concat(tree.path.substring(oldPrefix.length)))
  .where(tree.path.startsWith(oldPrefix))
  .execute()
```

- **장점**
  - 서브 트리·이동 모두 1쿼리
  - 표준 SQL 이라 DB 종속 없음
  - QueryDSL 로 전부 표현 가능
- **단점**
  - 비정규화 → 이동 시 자손 전체 갱신 필요
  - 깊이가 컬럼 길이로 제한
  - 형제끼리 `path` 동일 → `path` 만으로 노드 특정 불가
- **주의사항**
  - `path` 인덱스 필수(선행 `%` 금지)
  - `parent_id` 와 `path` 정합성은 애플리케이션 책임
  - bulk UPDATE 뒤 `entityManager.clear()`

### 3-4. Recursive CTE

- `with recursive` 로 자식을 따라 내려가는 재귀를 **DB 안에서** 처리 → 왕복 1회로 서브 트리 전체 조회
- 추가 컬럼 없음 → `parent_id` 만 있는 기존 스키마에 그대로 적용
- 3-2 의 while 루프를 SQL 로 이관한 형태

```sql
with recursive sub(id, name, path, parent_id) as (
  select n.id, n.name, n.path, n.parent_id from tree n where n.id = :id
  union all
  select c.id, c.name, c.path, c.parent_id from tree c join sub s on c.parent_id = s.id
)
select * from sub
```

**타입세이프 조립** — QueryDSL JPA 는 CTE 불가. JPQL 에 `with` 절이 없어 표현 방법 없음. Hibernate Criteria 의 `withRecursiveUnionAll` 은 의존성 추가 없이 재귀 CTE 조립 가능

```kotlin
val anchor = builder.createQuery(String::class.java)
val start = anchor.from(Tree::class.java)
anchor.select(start.get<String>("id").alias("id")).where(builder.equal(start.get<String>("id"), id))

val subtree = criteria.withRecursiveUnionAll(anchor) { self ->
  val step = builder.createQuery(String::class.java)
  val child = step.from(Tree::class.java)
  val parent = child.join(self)
  parent.on(builder.equal(child.get<Tree>("parent").get<String>("id"), parent.get<String>("id")))

  step.select(child.get<String>("id").alias("id"))
}
```

- CTE 는 **엔티티가 아니라 튜플** — 재귀 항에 엔티티를 통째로 전달하면 `SingleTableEntityPersister cannot be cast to TableGroupJoinProducer`
- id 만 담고 바깥에서 엔티티를 `in` 으로 조회하는 형태(테이블 2회 접근, 쿼리는 1회)
- 각 select 항목에 `alias` 필수 — 누락 시 `aliases are required in CTEs`
- CTE 정의는 root query 필수 — `criteria.subquery(...)` 전달 시 `expecting a root query to use as CTE instead found a subquery`

**Blaze-Persistence** — QueryDSL 문법으로 CTE 를 쓰는 유일한 방법이나 이 스택에서는 불가

- Hibernate 통합이 6.2 까지 → Hibernate 7 에서 제거된 `engine.spi.Mapping` 참조로 부팅 실패
- querydsl 통합이 원본 `com.querydsl:querydsl-jpa` 에 의존 → openfeign 포크와 패키지 충돌
- 적용하려면 Spring Boot·Hibernate 다운그레이드 + QueryDSL 포크 교체 필요

- **장점**
  - 스키마 변경·비정규화 없음
  - 이동은 `parent_id` 변경만으로 완료, 자손 갱신 없음
  - 깊이 제한 없음
  - 재귀 항에 `depth` 추가 시 레벨 제한 조회 가능
- **단점**
  - QueryDSL 표현 불가, DB 방언 종속
  - 깊이만큼 인덱스 조회 반복 → 서브 트리가 크면 mpath 의 단일 범위 스캔보다 느림
  - 순환 데이터 존재 시 무한 재귀
- **주의사항**
  - H2·Oracle 은 CTE 컬럼 목록 필수, `select *` 는 문법 오류
  - 컬럼 추가 시 CTE 도 동시 수정 필요(엔티티 매핑 누락 위험)
  - MySQL 은 8.0+, `cte_max_recursion_depth` 기본 1000

---

## 4. 비교

| | 서브 트리 | 삽입 | 이동 | 깊이 제한 | QueryDSL |
| --- | --- | --- | --- | --- | --- |
| 재귀 순회 | **N+1** | 1 | 1 | 없음 | O |
| 레벨 단위 IN | 깊이+2 | 1 | 1 | 없음 | O |
| **Materialized Path** | **1** (범위 스캔) | 1 | **1** (bulk) | path 길이 | O |
| **Recursive CTE** | **1** (깊이만큼 재귀) | 1 | **1** (parent_id 만) | 없음 | X (Criteria 로 대체) |

- 스키마 변경 불가 또는 이동이 잦으면 **CTE**
- 읽기가 압도적이고 서브 트리가 크면 **mpath**
