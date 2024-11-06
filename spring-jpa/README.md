## JPA core

## deleteAll, deleteAllInBatch

### deleteAll
- 100 개 데이터 deleteAll() -> 100번의 delete 쿼리
```java
@Override
@Transactional
public void deleteAll() {

	for (T element : findAll()) {
		delete(element);
	}
}
```

### deleteAllInBatch
- 엔티티 이름을 기반으로 해당 테이블을 찾아 delete 쿼리
- `delete from {엔티티명}`
```java
@Override
@Transactional
public void deleteAllInBatch(Iterable<T> entities) {

	Assert.notNull(entities, "Entities must not be null!");

	if (!entities.iterator().hasNext()) {
		return;
	}

	applyAndBind(getQueryString(DELETE_ALL_QUERY_STRING, entityInformation.getEntityName()), entities, em)
			.executeUpdate();
}

//public static final String DELETE_ALL_QUERY_BY_ID_STRING = "delete from %s x where %s in :ids";
```

### 모든 필드가 null 인 @Embeddable NPE 이슈
```kotlin
@Embeddable
class LastSpace(
  @Enumerated(EnumType.STRING)
  @Column(name = "last_space_type", length = ColumnSizeConstants.SPACE_TYPE)
  var spaceType: UserSpaceType? = null,

  @Column(name = "last_space_id", length = ColumnSizeConstants.UUID)
  var spaceId: String? = null
)
```
- 위와 같이 모든 필드가 null 인 Embeddable 필드의 경우 영속화시 해당 필드는 null 이 되어 참조시 NPE 가 발생한다.

```
https://in.relation.to/2016/02/10/hibernate-orm-510-final-release/

@Embeddables and all null column values
Historically Hibernate would always treat all null column values for an @Embeddable to mean that the @Embeddable should itself be null. 5.1 allows applications to dictate that Hibernate should instead use an empty @Embeddable instance. This is achieved via an opt-in setting: hibernate.create_empty_composites.enabled.

See HHH-7610 for details.
```

- `spring.jpa.properties.hibernate.create_empty_composites.enabled=true`



사용시 warning log
- https://hibernate.atlassian.net/jira/software/c/projects/HHH/issues/HHH-11936

```
HHH000483: An experimental - and now also deprecated - feature has been enabled (hibernate.create_empty_composites.enabled=true) that instantiates empty composite/embedded objects when all of its attribute values are null. This feature has known issues and should not be used in production. See Hibernate Jira issue HHH-11936 for details.
Standard Commons Logging discovery in action with spring-jcl: please remove commons-logging.jar from classpath in order to avoid potential conflicts
```

### Persistable
- save() 메서드의 경우 pk 유무에 따라 동작이 달라진다.
  - pk가 없는 경우 persist (insert)
  - pk가 있는 경우 merge
    - pk 기준 조회 후 존재하는 경우 update, 없는 경우 insert (비효율)
- entity 에서 Persistable 을 구현시 isNew 메서드가 true 를 반환하는 경우 식별자 유무와 관계없이 persist 를 수행한다.
- 주로, entity 의 pk 를 직접 할당하는 경우 사용한다.
```kt
override fun getId() = id
override fun isNew() = createdDate == null
```
