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

- 1번 이슈의 1,111회가 **깊이 + 2 = 6회**로 줄어든다. 노드가 10배가 되어도 6회
- **장점** — 스키마 변경 없음. 서브트리만 읽는다. `parent_id` 인덱스만 있으면 되고, 인접 리스트로 할 수 있는 최선
- **단점** — 깊이만큼 왕복. 마지막 레벨은 항상 빈 결과 1회 낭비
- **주의사항** — `in` 파라미터가 레벨 폭만큼 늘어난다. 한 레벨이 수천 개면 청크로 나눠 조회

### 3-3. Materialized Path (mpath)

- 각 노드가 루트부터 **자기 부모까지**의 id를 이어붙인 문자열을 `path` 컬럼에 따로 저장
- `path + 자기 id + "/"` 를 prefix 로 `startsWith` 검색 1번이면 자식·손자·그 아래까지 전부 나온다. 계층 구조는 메모리에서 묶는다(3-2와 동일한 조립)

```
r     /                 -- 실제로는 id. 읽기 쉽게 이름으로 표기
a     /r/
a1    /r/a/
a1x   /r/a/a1/
```

**조회** — 자손만 나오므로 이미 로드한 시작 노드를 앞에 붙인다

```kotlin
val descendants = query.selectFrom(tree)
  .where(tree.path.startsWith(node.path + node.id + "/"))
  .fetch()

TreeView.assemble(listOf(node) + descendants)
```

**저장** — 생성 시점에 `부모 path + 부모 id`. 부모가 없으면 `/`. 자기 id 를 몰라도 되므로 id 생성 전략이 자유롭고, 추가 조회 없이 INSERT 1번

```kotlin
path = parent?.let { it.path + it.id + "/" } ?: "/"
```

**갱신(이동)** — 자기 `path` 는 새 부모 기준으로 다시 만들고, 자손 전체는 prefix 치환 bulk UPDATE 1번

```kotlin
node.moveTo(newParent)

query.update(tree)
  .set(tree.path, Expressions.asString(newPrefix).concat(tree.path.substring(oldPrefix.length)))
  .where(tree.path.startsWith(oldPrefix))
  .execute()
```

- **장점** — 서브트리·이동 모두 1쿼리. 조상 id는 `path` 를 자르면 조회 없이 나온다. 표준 SQL이라 DB 종속 없고 QueryDSL로 전부 표현된다
- **단점** — 비정규화라 이동 시 자손 전체 갱신 필요. 깊이가 컬럼 길이로 제한된다. 형제끼리 `path` 가 같아 `path` 만으로 노드를 특정할 수 없다
- **주의사항** — `path` 인덱스 필수(선행 `%` 금지). `parent_id` 와 `path` 의 정합성은 애플리케이션 책임. bulk UPDATE 뒤 `entityManager.clear()`

---

## 4. 비교

| | 서브트리 | 조상 | 삽입 | 이동 | 깊이 제한 | QueryDSL |
| --- | --- | --- | --- | --- | --- | --- |
| 인접 리스트 재귀 | **N+1** | N | 1 | 1 | 없음 | O |
| 레벨 단위 IN | 깊이+2 | 깊이 | 1 | 1 | 없음 | O |
| **Materialized Path** | **1** (범위 스캔) | **1** | 1 | **1** (bulk) | path 길이 | O |
