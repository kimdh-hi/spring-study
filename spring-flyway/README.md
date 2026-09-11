## Spring flyway

### Flyway
- https://documentation.red-gate.com/flyway
- Redgate 에서 개발한 데이터베이스 마이그레이션(스키마 버전 관리) 도구
- 데이터베이스 변경사항에 버전을 부여하고 배포 자동화 하는 것으로 목적으로 함
- 동작 원리
  - flyway는 스키마 히스토리 테이블을 관리 (flyway_schema_history)
  - flyway_schema_history 를 통해 테이블 변경 이력을 추적
  - 기본적으로 flyway_schema_history 의 변경사항 기록 중 성공한 가장 높은 버전을 찾고, 해당 버전보다 높은 버전을 버전 순차대로 migrate 적용
- 일반적으로 spring flyway 의 경우 서버 구동시 `migrate` 가 실행되므로 버전별 flyway script 만 관리한다면 DB 변경사항 이력관리와 마이그레이션 자동화 가능

### script prefix
- flyway 스크립트 파일명은 `접두사 + 버전 + 구분자 + 설명 + 확장자` 구조
- 접두사가 스크립의 종류를 결정
  - V(Versioned), R(Repeatable), U(Undo)

#### V (Versioned)
- 각 스크립트는 정확히 한 번만 적용
- 버전은 반드시 고유해야 하고, 숫자순 정렬됨 (점 표기법, 언더스코어 표기법 모두 허용)
- 버전은 단순 증가되는 정수 또는 타임스탬프 등 팀 컨벤션에 맞추어 지정
- `__` 는 버전과 설명 부분을 구분 (커스텀 가능하고 `__` 가 기본값)
- migrate 이후 이전 스크립트 파일 내용에 수정이 생기는 경우 체크섬이 달라져 migrate 실패
- 테이블, 인덱스, 제약조건 생성 및 변경, 컬럼 추가/삭제 및 일회성 데이터 보정 시 사용
  - 거의 일반적으로 V만 사용.

```
V1__migration.sql
V202609111322__migration.sql
V001.002__migration.sql
V2_202609111322__migration.sql
```

#### R (Repeatable)
- 버전 없음
- 파일 내용이 바뀌어 체크섬이 달라질 때마다 실행
- Versioned script migrate가 모두 끝난 이후 R script 순차적으로 실행
  - 여러 개 인 경우 파일명 중 description 순으로 실행 
- 파일 내용이 바뀌면 flyway 는 다시 실행하므로 멱득성은 사용자가 보장해야 함
- 뷰, 프로시저, 함수, 트리거 관리시 사용

```
R__repeatable.sql
```

#### U (Undo)
- `migrate` 가 아닌 `undo` 명령으로 실행
  - `undo`: 기본적으로 마지막 적용 versioned 로 롤백
- Teams 이상 티어 사용가능 Community 사용불가
- 사용할 일 없을 듯.

```
U1__migrate.sql
U001.002__migrate.sql
```

#### B (Baseline)
- 쌓인 script 압축
- 오랜 기간 flyway 를 사용하게 되면 수많은 V, R 파일들이 쌓일 수 있음
  - 로컬 DB에 구성시 script 수백개를 순서대로 실행해야 함.
  - validate 시 수백개 체크섬을 검사해야 함.
  - 그냥 좀 지저분함.
- 현재 스키마 전체를 덤프해서 파일 하나로 생성
  - `B202609111342__baseline.sql`
  - 202609111342 이후 버전부터 migrate, validate 대상이 됨.
- baseline 이전 버전 기존 script들은 별도 경로에 archive 

```
B001__baseline.sql
B20240601__production_snapshot.sql
```


### flyway_schema_history
- 언제, 누구에 의해 스키마 변경사항이 적용되었는지 추적하기 위해 대상 스키마에 전용 이력 테이블을 생성
  - 테이블 이름: flyway_schema_history
- 빈 DB에서 실행시 flyway 는 flyway_schema_history 을 찾고 없으면 생성
```
mysql> select * from flyway_schema_history;
+----------------+----------+-------------+------+---------------------------+-------------+--------------+---------------------+----------------+---------+
| installed_rank | version  | description | type | script                    | checksum    | installed_by | installed_on        | execution_time | success |
+----------------+----------+-------------+------+---------------------------+-------------+--------------+---------------------+----------------+---------+
|              1 | 20230221 | init        | SQL  | V20230221__init.sql       |  1211654660 | root         | 2026-09-10 16:17:11 |             21 |       1 |
|              2 | 20230225 | add column  | SQL  | V20230225__add_column.sql | -1105085253 | root         | 2026-09-10 16:17:11 |             26 |       1 |
|              3 | NULL     | sample data | SQL  | R__sample_data.sql        | -2132993113 | root         | 2026-09-10 16:17:11 |             10 |       1 |
|              4 | NULL     | sample data | SQL  | R__sample_data.sql        | -2128645744 | root         | 2026-09-10 16:18:01 |             14 |       1 |
|              5 | NULL     | sample data | SQL  | R__sample_data.sql        | -2132993113 | root         | 2026-09-10 16:18:11 |             12 |       1 |
+----------------+----------+-------------+------+---------------------------+-------------+--------------+---------------------+----------------+---------+
```
- installed_rank
  -  적용 순서를 의미. 1부터 시작하는 일련번호이자 PK
- type
  - 마이그레이션 종류
  - SQL, JDBC, SCRIPT, BASELINE, DELETE, SCHEMA
- checksum
  - 스크립트 내용에 대한 CRC32 체크섬
  - 스크립트 변경여부 감지용도
- success
  - 성공 여부
  - 실패한 마이그레이션도 행으로 남을 수 있음
  - success false 가 있는 경우 이후 migrate 는 거부되고 repair 필요

### baseline-on-migrate
- https://documentation.red-gate.com/fd/baselines-273973441.html
- flyway_schema_history 테이블이 없는 상태에서 비어있지 않은 DB에 migrate 시 baseline 을 먼저 호출할지 여부를 결정
  - 이미 운영중인 DB를 flyway 관리 대상으로 편입시키기 위한 장치
  - 완전히 새로운 DB(greenfield) 대상으로는 baseline 신경쓸 것 없음.
- flyway script 중 특정 버전(V...)를 baseline 으로 설정하면 해당 script 이후 버전의 스크립트만 migrate 대상이 됨.
- 운영중인 DB에 flyway 최초 도입시 migrate 시 한 번 사용
  - 운영중인 db의 현재 상태의 DDL을 v1 flyway script 에 기록하고, baseline은 1(v1) 으로 설정한다.
  - 이후 스키마 변경사항은 v1 이후 버전으로 작성하면 v1 이후 버전(baseline 이후 버전)부터 migrate 대상이 된다.
- 운영 DB에 flyway 도입시 1회만 사용하고 baseline-on-migrate 설정은 비활성화 필요
- default: false
- baseline 활성화 시 동작
```
1. flyway_schema_history 테이블 생성
2. baselineVersion(default: 1) 으로 BASELINE 행 insert
3. baselineVersion 이하 마이그레이션은 이미 적용된 것으로 간주하고 스킵
4. baselineVersion 보다 높은 버전의 마이그레이션만 실행
```
- 관련 설정 예시
```
spring:
  flyway:
    baseline-on-migrate: true
    baseline-version: 20260910
    baseline-description: "Existing production schema"
```

### vs hibernate ddl-auto: update
- 우선 `ddl-auto: update`는 엔티티와 DB 스키마 싱크를 맞추지 않고, 엔티티에 있는데 DB에 없는 것들만 만들어 붙이는 방식이다.
  - 반대 방향으로 DB에 있는 Entity 에 없는 것은 맞추지 못한다.
  - 엔티티 필드 삭제시 => 아무것도 안 함
  - 엔티티 필드 rename 시 => rename 되는 컬럼 신규 추가하고 기존 컬럼 유지됨 (데이터 이관도 없음. 신규컬럼으로 조회하면 null)
  - @Column length 조정 => 아무것도 안 함
  - ...
  - ddl-auto: validate 의 경우 기동 시점에 예외를 발생시켜주지만, update 는 조용히 오동작 발생됨.
- ddl-auto: update 는 배포 전 실제 실행될 DDL을 알 수 없음.
- 이러한 이유로 개발, 테스트 환경 외에 update 비권장.

### flyway + ddl-auto: validate
- flyway 가 마이그레이션을 끝내기 전에 validate가 먼저 돌아서 실패하지 않나?
- https://docs.spring.io/spring-boot/how-to/data-initialization.html
- `ddl-auto: validate` 는 `AbstractEntityManagerFactoryBean` 빈 초기화 시점에 실행됨
- `DatabaseInitializationDependencyConfigurer` 통해 dependsOn 조정하여 flyway 마이그레이션이 선행되는 것을 보장한다.
- 단, `defer-datasource-initialization: true` 사용중인 경우 위 보장이 안 됨
- flyway 사용시 `ddl-auto: validate` 지정하지 않는 경우 none 이므로 영향 없겠지만, validate 사용시 defer-datasource-initialization: true 지정시 migrate, validate 순서 보장 안되는 것에 주의할 것.

### 참고
- https://documentation.red-gate.com/fd/flyway-concepts-271583830.html
- https://tecoble.techcourse.co.kr/post/2021-10-23-flyway/
- https://www.red-gate.com/hub/product-learning/flyway/managing-static-data-in-flyway-database-development/