

### MySql Index

#### 자료구조
- `B+Tree` 를 사용한다.
  - `B+Tree`
    - 삽입, 삭제시 추가작업을 통해 트리는 균형을 이룬다.
    - 하나의 노드가 여러 개 자식 노드를 가질 수 있다.
    - 리프노드에만 데이터가 존재한다.
      - MySql 의 경우 리프노드에 존재하는 데이터는 `pk` 이다.
      - Oracle 의 경우 리프노드에 데이터의 주소값이 저장된다.

#### 클러스터드 인덱스 
- 클러스터드 인덱스는 데이터의 위치에 대한 키 값이다.
  - 클러스터드 키의 순서에 따라 데이터 저장 위치가 변경되므로 클러스터드 키의 삽입, 삭제시 성능상 이슈가 발생할 수 있다.
- MySql 에서 모든 PK 는 클러스터드 인덱스이다. (PK == 클러스터드 인덱스)
  - 바꿔말하면 PK 순서에 따라 데이터 저장 위치가 변경되므로 PK 삽입, 삭제시 성능상 이슈가 발생할 수 있다.
  - `PK Auto_increment vs UUID ...`
- MySql 에서 PK 테이블을 제외한 모든 인덱스 테이블은 PK 를 값으로 저장한다.
  - PK 의 크기가 인덱스의 크기를 결정한다.

PK 를 제외한 나머지 인덱스를 지칭하는 세컨더리 인덱스만으로 데이터를 찾아갈 수 없다.
세컨더리 인덱스의 검색결과로 찾아지 것은 결국 PK 이다.
결국 PK 인덱스를 타게 된다.

세컨더리 인덱스는 지정한 인덱스 키값으로 정렬되고 그 값으로는 PK를 저장한다.
인덱스 키값으로 B-Tree 를 순회하며 리프노드에 도달했을 때 결국 찾아지는 값은 PK 인 것이다.

세컨더리 인덱스의 리프노드에서 찾은 PK 로 다시 PK 인덱스 테이블을 순회하며 PK 인덱스 테이블의 리프노드에 도달했을 때 실제 데이터의 주소를 찾게 된다.


#### 인덱스 적용시 주의

1. 인덱스 대상 필드를 가공할 경우 인덱스를 타지 못한다.

```sql
select *
from member
where age * 10 = 1
```
값은 그대로 인덱스의 키 값으로 사용되기 때문에 가공시 인덱스가 적용되지 않는다.

```sql
select *
from member
where age = '1';
```
인덱스를 사용하기 위해 조건을 걸 때 타입을 맞춰줘야 한다.<br/>
`age` 가 int 타입인 경우 위 쿼리문은 인덱스를 타지 못한다

2. 복합 인덱스

PK 인덱스를 제외한 세컨터리 인덱스들은 값을 `인덱스 키` 로 하고 PK 를 값으로 한다.<br/>
정렬은 `인덱스 키` 로 수행하고 동일한 `인덱스 키` 에 대해서는 PK 를 기준으로 정렬된다.

복합 인덱스는 두 개의 `인덱스 키` 를 갖는다.<br/>
`선두 인덱스 키` 기준으로 먼저 정렬되고 `선두 인덱스 키` 가 동일한 경우 다음 인덱스 키를 기준으로 정렬된다.

`선두 인덱스 키`는 단독으로 인덱스 조건에 사용될 수 있지만 `이후 인덱스 키`는 선두 인덱스 키 없이는 인덱스를 타지 못한다.<br/>
`선두 인덱스 키` 를 기준으로 정렬되고 동률인 경우 `두 번째 인덱스 키` 가 사용되는 것이므로 `두 번째 인덱스 키`로 정렬되지는 않았기 때문이다.

3. 하나의 쿼리는 하나의 인덱스만 탄다.

기본적으로 하나의 쿼리로 여러 인덱스 테이블을 사용하지 않는다.<br/>
(`index merge hint` 를 사용하면 여러 인덱스 테이블을 사용할 수 있다..)

---

### 오프셋 기반 페이지네이션의 문제

1. 마지막 페이지를 알아야 함으로 데이터의 전체건수 (totalElements) 를 알아야 함
`데이터가 많아질수록 전체 데이터 수를 구하는 것은 부담`이다.

일부 대용량 데이터를 다루는 시스템에서 offset 기반 페이징을 지원하는 경우 마지막 페이지에 대한 바로 접근을 막거나 스크롤 형태로 데이터를 제공한다.

2. 불필요 한 데이터 조회
offset 기반으로 데이터를 읽을 때 offset 이전까지 데이터를는 사용하지 않지만 읽어야 한다.
데이터가 많아지고 offset 크기가 커지는 만큼 읽고 사용되지 않는 데이터가 많아진다.

위 문제를 해결하기 위해 `커서 기반 페이징`을 사용한다.

### 커서 기반 페이징

커서 기반 페이징은 offset 이 아닌 key 를 기반으로 데이터를 읽기 시작한다.
key 에 해당하는 데이터부터 size 만큼 읽는 것이다.
offset 처럼 offset 이전까지 데이터를 읽을 필요가 없다.
서버는 데이터를 size만큼 읽고 다음 key를 함께 응답한다.

but, 커서 기반 페이징은 전체 데이터를 조회하지 않으므로 totalElements 를 포함할 수 없다.
즉, 마지막 페이지에 대한 바로 접근 기능을 제공할 수 없다.

++<br/>
JPA를 사용한다면 Slice 타입을 사용하는 방법도 있다.<br/>
key로 사용할 키 값이 적당하지 않다면 ex)UUID PK <br/> 
리턴타입을 `Page` 대신 `Slice` 를 사용할 수 있다.
위에서 말한 커서 기반 페이징처럼 시작위치까지의 read를 없앨 수는 없지만 count 쿼리가 발생하지 않기 때문에 보다 나은 성능을 가질 수 있다.

```kotlin
// slice 방식 페이징 
fun <T> getSlice(
  query: JPAQuery<out T>,
  pageable: Pageable
): SliceImpl<T> {
  val list = query
    .offset(pageable.offset)
    .limit((pageable.pageSize + 1).toLong())
    .fetch()

  return SliceImpl(list, pageable, hasNext(list, pageable.pageSize))
}

private fun <T> hasNext(list: MutableList<T>, pageSize: Int): Boolean {
  return if(list.size > pageSize) {
    list.removeAt(pageSize)
    true
  } else {
    false
  }
}
```

---

### ACID

Atomicity 원자성
- `mysql` 은 원자성을 `mvcc` (다중 버전 동시 제어) 를 통해 보장한다.
- 커밋 전, 변경되기 이전 데이터(원본 데이터)는 `undo log` 영역에 저장된다.
- 트랜잭션이 실패한다면 `undo log` 에 있는 이전 원본 데이터가 다시 원본 데이터가 된다.
- 트랜잭션이 성공하여 커밋된다고 해도 `undo log` 영역의 원본 데이터는 바로 삭제되지 않는다.
- 기본적으로 각 sql 문마다 트랜잭션이 걸리게 된다.

Consistency 일관성
- 트랜잭션이 종료됐을 때 데이터의 무결성이 보장되어야 한다.
- 제약조건을 통해 일관성을 보장한다.
  - 유니크, 외래키 ...
  - 유니크 제약조건이 걸린 경우 어떤 트랜잭션이든 유니크 제약조건을 깰 수 없도록 보장한다.

Isolation 독립성
- 각 트랜잭션은 서로 간섭하지 않고 독립적으로 동작한다.
  - but, 독립성을 보장할수록 성능은 포기해야 한다. (trade-off...)
- 트랜잭션 격리레벨

Durability 지속성
- 완료된 트랜잭션은 유실되지 않고 지속되어야 한다.
- WAL (Write-ahead logging)
  - DB 성능의 핵심은 랜덤 디스크 I/O 를 줄이는 것이다.
  - 쓰기 작업시마다 디스크에 데이터를 넣는 것은 성능상 좋지 않다.
  - 때문에 디스크에 바로 쓰지않고 메모리에 데이터를 저장한다. 근데 메모리에 있는 쓰기작업에 의한 데이터를 디스크에 밀어넣기 전 DB서버가 죽는다면?
    - 메모리에만 올라간 디스크에 반영해야 할 데이터가 유실될 수도 있다.
  - MySQL은 WAL을 통해 위와 같은 문제가 발생하는 것을 방지한다.
    - DB 재부팅? 시 WAL을 보고 메모리에 있는 데이터를 디스크에 반영한다.

---

### 트랜잭션 격리레벨

- READ UNCOMMITTED
  - Dirty read, Non-repeatable read, Phantom read 모두 발생
- READ COMMITTED
  - 커밋된 데이터만 읽으므로 Dirty read 를 방지한다.
  - Non-repeatable read, Phantom read 발생 
- REPEATABLE READ
  - 커밋된 데이터만 보면서 트랜잭션마다 ID를 부여하고 더 나중에 들어온 트랜잭션에 의한 결과는 보지 않는다.
  - 현재 트랜잭션 이후 트랜잭션의 결과 데이터는 보지않기 때문에 Non-repeatable read 방지
  - Phantom read 발생
- SERIALIZABLE READ
  - 3개 이상현상 모두 방지

READ UNCOMMITTED, SERIALIZABLE READ 는 성능과 안정성의 극단적 트레이드 오프로 잘 사용되지 않는다.<br/>
REPEATABLE READ 의 경우 데드락 문제가 꽤나 빈번하므로 주의가 필요하다. <br/>
READ COMMITTED 가 가장 흔하게? 사용된다.

이상현상
- Dirty read
- Non-Repeatable read
- Phantom read

(InnoDB 기준) 4개 트랜잭션 격리 레벨은 결국 3가지 이상현상을 몇 가지를 방지하는지 몇 가지를 허용하는지에 대한 것이다.

#### Dirty read
커밋되지 않은 데이터를 읽는 이상현상

tx1
- A 잔액 2000원, B 잔액 `1000`
- A -> B 에게 1000원 송금 -> A: 1000, B: 2000
- 송금 실패 (롤백) -> A: 2000, B: `1000`

tx2
- A -> B 에게 1000원을 송금하고 송금이 실패하기 전 B의 잔액을 read
- tx2 가 읽은 B의 잔액은 `2000`

#### Non-repeatable read
같은 트랜잭션 내에서 같은 데이터를 읽었는데 결과가 다른 경우

- `tx1` 은 한 개 트랜잭션 동안 총 2번 A의 잔액을 읽을 것임
- 최초 A의 잔액은 `1000`
- `tx1`의 첫 번째 read 결과는 `1000`
- `tx2` 가 A의 잔액을 업데이트 (+1000) 후 커밋 
  - A 의 잔액은 `2000`
- `tx1` 은 `tx2` 의 트랜잭션이 커밋되고 다음 read가 수행됨
  - `tx1` 의 두 번째 read 결과는 `2000`

`tx1` 은 한 개 트랜잭션 동안 서로 다른 A 의 잔액을 조회하게 됨

#### Phantom read
같은 트랜잭션 내에서 같은 조건으로 조회했을 때 결과가 다른 경우

- `tx1` 은 한 개 트랜잭션 동안 잔액이 `1000` 이상은 사용자를 조회
  - 최초 A: 1200 B: 500
  - 첫 번째 조회결과 A만 조회됨
- `tx2` 가 B의 잔액을 `1500`으로 갱신 (+1000) 후 커밋
- `tx1` 은 같은 조건 (잔액 1000원 이상)으로 조회
- A, B 모두 조회

---

### 동시성

동시성 테스트
- breakpoint 설정
- 우클릭 - Suspend - Thread

#### 비관적 락
- 읽기 락(sharedLock): `select ... for share`
- 쓰기 락(exclusiveLock): `select ... for update` or update, delete 쿼리

MySQL 에서 Lock 은 row 가 아니라 인덱스를 잠근다.
```
Lock 종류
테이블 락, *레코드 락, 갭 락 ...
```
따라서 인덱스가 없는 조건으로 Locking Read 시 불필요한 데이터들까지 잠길 수 있다.
```
where 절에 인덱스 필드와 인덱스가 아닌 필드가 사용된 경우
예상하기로는 조회된 결과 레코드만 락이 잡힐 것 같다.

but, 인덱스가 아닌 필드의 조건은 무시되고 인덱스 필드에 의해 조회된 모든 레코드가 잠긴다.
```

예시
```sql
start transaction 
select * from user where teamId = 't1' and type = 'USER' for update;
commit;

teamId 는 인덱스 필드라고 가정
teamId 가 't1' 인 모든 레코드가 락을 대상이 된다.
```

MySQL 락 확인
```sql
select * from performance_schema.data_locks;

select * from information_schema.innodb_trx;
```




#### 낙관적 락
- 동시성 이슈가 비교적 빈번하지 않은 경우, 비관적 락으로 인한 성능저하가 우려되는 수준인 경우
- CAS(Compare and Set) 연산을 통해 애플리케이션 수준에서 동시성을 제어
- 레코드 (row) 마다 버전(버전필드)을 부여하고 갱신시 내가 조회한 버전이 맞는지 확인 
  - Spring data jpa - @Version
- 비관적 락에 비해 동일한 트랜잭션에 묶을 필요도, `FOR UPDATE` 와 같은 쿼리를 사용할 필요도 없기 때문에 구현과 성능상 모두 유리하다.








