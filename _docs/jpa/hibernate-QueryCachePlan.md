# hibernate QueryCachePlan

## QueryPlanCache

- HQL/JPQL/Criteria 와 같은 엔티티 쿼리 실행 전 컴파일 결과를 저장하는 캐시

```
query compile

1. 쿼리 문자열 파싱
2. AST 생성
3. 파라미터 식별
4. 반환타입 식별
5. 결과 매핑 계획 수립
```

> Acts as a cache for compiled query plans, as well as query-parameter metadata.
https://docs.hibernate.org/orm/5.5/javadocs/org/hibernate/engine/query/spi/QueryPlanCache.html
> 
- 엔티티 쿼리(JPQL/Criteria)의 경우 쿼리 문자열을 파싱해 AST 를 만들고 메타데이터(파라미터, 반환타입) 를 결정하고 SQL 을 생성하는 과정이 비싸므로 캐싱 효율 좋음
- native query 의 경우 컴파일 없이 메타데이터(파라미터, 반환타입) 만 추출, 저장하므로 캐싱 효율이 상대적으로 적음

## QueryPlanCache(`QueryInterpretationCache`)저장소

- hibernate 7.1 이전
    - `BoundedConcurrentHashMap` 사용
        - `ConcurrentHashMap` 에 최대 크기제한 (bound) 와 evict 기능을 더한 동시성 map
        - default 엔트리 수 제한: 2048 개
        - 엔트리 수 제한일 뿐 메모리에 대한 제한이 아님.
            - 거대한 in 절 혹은 bulk insert 와 같이 파라미터가 수천개인 쿼리의 경우 plan 하나가 mb 단위가 될 수 있음
            - 즉, 엔트리 수 제한에 걸려 evict 되기 전 OOM 발생 가능.
- hibernate 7.1 이후 (7.2.7.final 기준)
    - `BoundedConcurrentHashMap` deprecated
        - `LegacyInternalCacheImplementation`
        - https://hibernate.atlassian.net/browse/HHH-19544
    - deprecated 되었지만 대체 구현체는 제공되지 않음. (`InternalCache` 주석 참고)
        - 추후 서드파티 캐싱 라이브러리 통해 구현될 것을 예고….;
    - 결과적으로 달라진 건 없음.
        - `BoundedConcurrentHashMap` 가 아닌 다른 로컬 캐시로 확장 가능

## hibernate QueryPlanCache option

- `hibernate.query.plan_cache_max_size`
    - default: 2048
    - 캐시 최대 엔트리 수
    - 엔트리 수의 상한일뿐 메모리 용량과는 관계없음
- `hibernate.plan_parameter_metadata_max_size`
    - 파라미터 메타데이터 상한
- `hibernate.query.plan_cache_enabled`
- `hibernate.query.in_clause_parameter_padding`
    - hibernate 5.2.18 도입
    - in절 파라미터 개수를 2의 제곱으로 확장
    - 남는 자리는 마지막 파라미터 값으로 채움
    - in절 파라미터 수가 달라서 발생하는 plan 수 폭증을 억제
    - QueryPlanCache OOM 발생시 해결방안 중 하나

## QueryPlanCache 로 인한 OOM 발생시 해결안

1. `in_clause_parameter_padding` 옵션 적용
- 동일 select 절에 in 절 파라미터만 달라지는 것에 대해 각각 plan cache 생성 방지
1. `plan_cache_max_size` 하향 조정
- default: 2048
- 캐시 엔트리의 max 개수
- 캐시 각각의 요소가 거대한 in 절을 포함하는 요청이 있다면 2048 개 엔트리 도달 전 OOM 발생 가능
- OOM 발생 전 캐시 엔트리 max 수에 먼저 도달하도록 유도

## QueryPlanCache 통계

```yaml
//springboot
spring.jpa.properties.hibernate.generate_statistics=true

//hibernate
hibernate.generate_statistics=true
```

```kotlin
  val statistics = entityManager.unwrap(Session::class.java).sessionFactory.statistics
  statistics.queryPlanCacheHitCount
  statistics.queryPlanCacheMissCount
```

## reference

- https://www.baeldung.com/hibernate-query-plan-cache
- https://hibernate.atlassian.net/browse/HHH-19544
- https://docs.hibernate.org/orm/5.5/javadocs/org/hibernate/engine/query/spi/QueryPlanCache.html