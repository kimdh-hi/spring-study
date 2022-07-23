## SQL 튜닝 실행 계획 파헤치기
	
### sql 파일 복사
```
[ 복사하고자 하는 sql 파일이 있는 경로에서 실행 ]

"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -uroot -p < 파일이름.sql
```

***
<br>

### 실행 계획 수행

#### 기본 실행 계획 수행
- 실행 계획을 확인하는 키워드
  - `EXPLAIN`
  - `DESCRIBE`
  - `DESC`
```sql
[ 어떤 키워드를 사용해도 결과는 같다. ]

EXPLAIN sql문;
DESCRIBE sql문;
DESC sql문;
``` 

#### MySQL, MariaDB 실행 계획 수행
- SQL문 앞에 `EXPLAIN`을 추가하면 옵티마이저가 만든 시행 계획이 출력된다.
```sql
EXPLAIN
	select * from 사원
	where 사원번호 between 100001 AND 200000;
```
- 실행계획의 출력결과가 조금의 차이가 있음

#### 기본 실행 계획 항목 분석
- id, select_type, table, type, key ...

##### id
- SQL문이 수행되는 차례(순서)를 ID 표기한 것
- `id`가 작을수록 먼저 수행된 것이고 `id`가 같다면 `join`이 발생한 것으로 해석 가능하다.

##### select_type
- select 문의 유형을 출력하는 항목
- select 문이 `from` 절의 것인지, `서브쿼리` 인지, `Union` 으로 묶인 select 문인지 등의 정보

`SIMPLE` 
- 가장 단순한 `select` 구문

`PRIMARY` : 서브쿼리가 포함된 SQL문에서 첫번째 select 문에 대한 표시
- 서브쿼리를 감싸는 외부쿼리 or Union이 포함된 SQL문의 첫번째 select문
- ex) select 절에 스칼라 서브쿼리가 포함된 경우 외부 쿼리의 테이블에 먼저 접근한다는 의미의 `Primary`

`SUBQUERY` : 독립적으로 수행되는 서브쿼르
- select 절 스칼라 서브쿼리, where 절 중첩 서브쿼리에 해당

`DERIVED` : FROM 절에 작성된 서브쿼리 (인라인 뷰)

`UNION` : Union, Union all 로 합쳐진 select 문에서 첫번째 select를 제외한 나머지 select 절에 해당
- Union 절의 첫번째 select 문은 `Primary`에 해당한다.

`UNION RESULT` : `Union ALL`이 아닌 `UNION`으로 결합했을 때 출력된다.

`DEPENDENT SUBQUERY` : `Union`, `Union ALL`을 사용하는 서브쿼리가 메인 테이블의 영향을 받는 경우에 해당
- `Union`으로 연결되는 `서브쿼리의 첫번째 select`에 해당한다.
- `Union`을 사용하는 서브쿼리들이 메인 테이블로부터 값을 하나씩 공급받는 구조이기 때문에 성능적으로 불리하여 튜닝의 대상이 된다.

`DEPENDENT UNION`  
- `Union`으로 연결되는 `서브쿼리의 첫번째 이후 select`에 해당한다.
- `DEPENDENT SUBQUERY`와 마찬가지로 메인 테이블로부터 값을 하나씩 공급받는 구조이기 때문에 튜닝의 대상이 된다.

`UNCACHEABLE SUBQUERY`
- 메모리에 상주하며 재활용 될 서브쿼리야 재사용되지 못할 때 출력된다.
  - 해당 서브쿼리 안에 사용자 정의 함수, 사용자 정의 변수가 포함된 경우
  - `RAND()`, `UUID()` 등의 함수로 조회시 매번 결과가 달라지는 경우
- 자주 호출되는 SQL문이라면 서브쿼리의 결과를 메모리에 상주시키는 방향으로 튜닝 가능하다.

`MATERIALIZED`
- `IN` 절에 사용된 서브쿼리가 임시 테이블을 만들어서 조인 작업을 수행할 때 출력된다.
```sql
[ in절에서 급여 테이블을 임시테이블로 만들고 사원 테이블과 조인을 수행 ]
EXPALIN
select * from 사원
where 사원번호 IN (SELECT 사원번호 FROM 급여 WHERE 시작일자 > '2020-01-01');
```  

##### table
- 테이블명을 표시하는 항목 (테이블명 or 테이블 별칭)
- 서브쿼리나 임시테이블의 경우 `<subquery#>` , `<derived#>` 으로 출력된다.

##### partitions
- 실행 계획의 부가정보로 데이터가 저장된 논리적인 영역을 표시하는 항목이다.
- 전체 파티션에 접근하는 것보다 사전에 정해진 특정 파티션에 접근하는 것이 성능상 유리하다.
- 너무 많은 파티션에 접근하는 것으로 출력되면 파티션 정의에 대한 튜닝을 고려해야 한다.

#### type
- 테이블의 `데이터를 어떻게 찾을지`에 관한 정보를 제공하는 항목이다.
- 테이블의 처음부터 끝까지 전부 스캔할 것인지, 인덳를 통해 데이터를 찾아갈지 등을 해석

`system`
- 테이블에 데이터가 없거나, 한 개만 있는 경우 (성능상 최상의 type)

`const`
- 1건의 데이터만 출력되는 유형
- 고유 인덱스 or 기본키를 이용해서 빠르게 1건의 데이터에만 접근하므로 성능상 매우 유리
```sql
EXPLAIN
select * from 사원
where 사원번호 = 100;
```

`eq_ref`
- `드라이빙 테이블`과의 조인 키가 `드리븐 테이블`에 유일해서 조인 시 성능상 가장 유리한 경우이다.
```sql
EXPALIN
select 매핑.사원번호, 부서.부서번호, 부서.부서명
from 부서사원_매핑 as 매핑, 부서
where 매핑.부서번호 = 부서.부서번호 AND 매핑.사원번호 BETWEEN 100 AND 110;
```
- 