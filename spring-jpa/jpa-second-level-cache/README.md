## Hibernate second-level cache

하이버네이트는 1차 캐시를 제공한다.<br/>
1차 캐시는 하이버네이트 Session 이 열리고 닫히는 것과 생명주기가 같다.<br/> 
즉, 1차 캐시의 경우 영속성 컨텍스트 내에서 각 엔티티 인스턴스 단위로 캐싱된다.<br/>

2차 캐시의 경우 Session 단위가 아닌 SessionFactory 의 범위에서 동작한다.<br/>
동일한 SessionFactory 에서 생성된 모든 Session 이 공유하는 캐시다.<br/>

1차, 2차 캐시가 모두 동작하고 있는 경우
- 1차 캐시에 엔티티 인스턴스가 있다면 바로 반환
- 1차 캐시에 없다면, 2차 캐시에 해당 엔티티 인스턴스가 있는지 조회

2차 캐시 활성화
```yaml
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=
```

### 참고
https://www.baeldung.com/hibernate-second-level-cache <br/>
https://hazelcast.com/use-cases/hibernate-second-level-cache/ <br/>