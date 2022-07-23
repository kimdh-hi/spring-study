## 1. MySQL - MariaDB

- MySQL 최초 오픈소스로 개발되었지만 오라클에게 인수되고 라이선스 정책이 변경되었다.
- 이에 MySQL의 핵심 개발자들이 오픈소스를 지향하기 위해 만든 것이 MariaDB이다.

> MySQL 버전확인
```sql
select @@version;
```

> 2021 DB엔진 영향력 순위
```
1위: MySQL 58% 
2위: PostgreSQL 27%
3위: SQLite
5위: MariaDB 5%
```


### MySQL와 Oracle의 차이

#### 다중화 구조에서 구조적 차이
오라클 DB
- 하나의 스토리지를 다중화된 DB서버가 공유하는 방식 (마스터 - 마스터)
- 사용자는 어떤 DB서버에 접속해서 SQL을 날리더라도 같은 결과를 출력한다. (같은 스토리지가 대상이므로)

MySQL DB
- MySQL DB: DB서버마다 독립적으로 스토리지를 할당하는 방식 (마스터 - 슬레이브)
  - 마스터: 쓰기/읽기
  - 슬레이브: 읽기만
- DB서버마다 스토리지를 갖기 때문에 각자의 역할이 부여될 수 있음을 의미한다.
  - SQL 튜닝관점에서 쿼리가 수행되는 DB서버를 파악하면 보다 효과적으로 튜닝이 가능할 것이다.
- MySQL의 경우 쿼리 오프로딩이 적용되어 마스터 서버에서는 쓰기 트랜잭션이, 슬레이브 서버에서는 읽기 트랜잭션이 수행된다.
  - Select 쿼리에 대한 튜닝을 마스터 노드에서 수행 ??
  - Update, Insert 등 쓰기 트랜잭션 관련 쿼리에 대한 튜닝을 슬레이브 노드에서 수행 ??
  - DB 서버에 따라 튜닝의 결과가 제대로 나타나지 않을 것이다.


> 쿼리 오프로딩
```
쓰기 트랜잭션과 읽기 트랜잭션을 분리해서 DB 처리량을 증가시키는 기법이다.
쓰기 트랜잭션: Update, Insert, Delete    
읽기 트랜잭션: Select
```


#### Join의 차이
- MySQL의 경우 대부분의 Join을 `nested loop join` (중첩 루프 조인) 방식으로 처리
  - MySQL 8부터 해시조인을 제공
- Oracle의 경우 `nested loop join` 뿐만 아니라 `sort merge join`, `Hash join` 방식까지 제공 
- 조인 알고리즘에 대해서도 이후 자세히 ...


#### SQL 문법의 차이 

**NULL 대체값 처리**
- 칼럼을 조회할 때 해당 칼럼이 `null`인 경우 다른 값으로 대체하는 기능
- MySQL: `IFNULL`
```sql
select IFNULL(컬럼명1, '대체값') 칼럼명1 from 테이블;
```
- Oracle: `NVL`
```sql
select NVL(컬럼명1, '대체값') 칼럼명1 from 테이블;
```

**페이징의 차이**
- 테이블에서 5개 데이터 조회
- MySQL: `LIMIT`
```sql
select 칼럼명1 from 테이블
LIMIT 5;
```
- Oracle: `ROWNUM`
```sql
select 칼럼명1 from 테이블
WHERE ROWNUM <= 5;
```

**날짜**
- MySQL: `NOW()` (from 절 불필요)
```sql
select NOW() as date;
```
- Oracle: `SYSDATE` (from 절에서 가상테이블을 명시해줘야 함)
```sql
select SYSDATE as date 
FROM dual;
```

**조건문**
- MySQL: `IF`, `WHEN~THEN`
```sql
select IF(컬럼명1='apple', 'A', '-') 
from 테이블;
```
- Oracle: `DECODE`, `IF`, `CASE WHEN~THEN`
```sql
select DECODE(칼럼명1, 'apple', 'A', '-') 
from 테이블;
```

**날짜 형식**
- MySQL: `DATE_FORMAT()`
```sql
select DATE_FORMAT(NOW(), '%Y%m%d %H%i%s') as date;
```
- Oracle: `TO_CHAR()`
```sql
select TO_CHAR(SYSDATE, 'YYYYMMDD') as date
from DUAL;
```

**자동 증가값**
- MariaDB, Oracle은 `시퀀스`를 사용
- MySQL은 `auto_increment` 혹은 `시퀀스`를 사용한다.

- 1부터 9999까지 1씩 증가하는 시퀀스 생성
```sql
CREATE SEQUENCE 시퀀스명
increment by 1
start with 1
minvalue 1
maxvalue 9999
cycle
cache

// MySQL, MariaDB에서 다음 시퀀스 값 가져오기
SELECT NEXTVAL(시퀀스명); 

// Oracle에서 다음 시퀀스 값 가져오기
SELECT 시퀀스명.NEXTVAL from dual;
```


















































