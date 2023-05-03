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