## spring kotlin

---

### non-nullable entity id
- 일반적으로 jpa 환경에서 kotlin 사용시 entity 의 id 는 nullable 로 선언된다. 

```java
//SimpleJpaRepository.class
@Transactional
public <S extends T> S save(S entity) {
    Assert.notNull(entity, "Entity must not be null");
    if (this.entityInformation.isNew(entity)) {
        this.entityManager.persist(entity);
        return entity;
    } else {
        return (S)this.entityManager.merge(entity);
    }
}

//JpaRepository.class
@Override
public boolean isNew(T entity) {

    ID id = getId(entity);
    Class<ID> idType = getIdType();

    if (!idType.isPrimitive()) {
        return id == null;
    }

    if (id instanceof Number n) {
        return n.longValue() == 0L;
    }

    throw new IllegalArgumentException(String.format("Unsupported primitive id type %s", idType));
}
```
- save() 시 entity 의 id 가 isNew == true 인 경우에만 persist() 가 호출된다.
- isNew() 판단은 primitive 타입이 아닌 경우 null 여부로 판단된다.
- 즉, entity id 의 경우 칼럼 자체는 nullable=false 로 설정되지만 nullable 이 강제된다.
- 단순히 entity id 를 non-nullable 로 지정하는 경우 이슈
  - non-nullable 로 지정하는 경우 isNew == false 이므로 persist()가 아닌 merge() 가 호출되고, merge() 는 해당 id 존재여부 판단을 위해 select 쿼리 후 insert 쿼리를 수행한다.
  - 불필요한 select 쿼리가 발생된다.
- Persistable 구현 v1
  - save 시 isNew true 인 경우 persist 호출
    - isNew 초기값 true 세팅시 merge 아닌 persist 호출 가능
  - save 시 isNew false 인 경우 merge 호출
    - @PostLoad 통해 entity 생성이 아닌 조회를 통해 load 된 경우 isNew false 세팅
    - - `UserRepositoryTest.saveFindAndDelete` 테스트 참고
  - delete 시 isNew=true 인 경우 delete 수행 안 됨
    - persist 된 entity 대상 delete 시 삭제되도록 @PostPersist 이벤트 시 new false 갱신
    - persist 이후 find (@PostLoad) 없이 entity 제거는 일반적이지는 않음. 
    - `UserRepositoryTest.saveAndDelete` 테스트 참고
- Persistable 구현 v2 ✅
  - 일반적으로 jpa auditing 기능은 사용 (레코드 생성시간 기록 등 위함)
  - createdAt (엔티티 persist 시 값 세팅, @CreatedDate)
  - isNew 기준 createdAt null 여부로 판단
```java
@Transactional
public <S extends T> S save(S entity) {
    Assert.notNull(entity, "Entity must not be null");
    if (this.entityInformation.isNew(entity)) {
        this.entityManager.persist(entity);
        return entity;
    } else {
        return (S)this.entityManager.merge(entity);
    }
}

@Override
@Transactional
@SuppressWarnings("unchecked")
public void delete(T entity) {

    Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);

    if (entityInformation.isNew(entity)) {
        return;
    }

    if (entityManager.contains(entity)) {
        entityManager.remove(entity);
        return;
    }
}
```

---

### value class 사용시 이슈
- sample entity: `Group.kt`

issue1
```
repository 생성시 id type 지정 이슈
- https://github.com/spring-projects/spring-data-jpa/issues/2840
- value class type 이 아닌 value class 내부 값의 타입 명시 (GroupRepository.kt 참고)
```

issue2
```
mockMvc pathVariable id class 포함시 toString() 호출 이슈
- UserControllerTest.pathVariableTest 참고
   * Expected :user-01
   * Actual   :UserTestId(value=user-01)
- value class toString override 추가 (UserTestId.kt 참고)
```


```kotlin
@MappedSuperclass
abstract class UuidPrimaryKeyEntity(
  @Id
  @Column(length = 50)
  @UuidGenerator
  private val id: String = "",
) : Persistable<String> {

  @Transient
  private var new: Boolean = true

  override fun isNew(): Boolean = new

  @PostLoad
  fun load() {
    new = false
  }
}

```
