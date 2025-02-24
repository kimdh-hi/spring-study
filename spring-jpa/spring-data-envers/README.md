## Spring data envers

envers 이전에는 별도 history 테이블을 만들고 CUD 연산시 해당 history 테이블을 갱신해줘야 했다.<br/>
envers 는 데이터 변경에 대해 추가적인 로깅작업을 쉽게 할 수 있도록 지원한다.


### `@Audited`
- 변경이력을 추적할 엔티티의 클래스 레벨에 추가
- `엔티티_이름_prefix` 히스토리 테이블 생성

### rev_type
- `0`: insert
- `1`: update
- `2`: delete

### @OnDelete(action = OnDeleteAction.CASCADE)
- 연관된 엔티티 삭제시 테이블 생성시 추가되는 `on delete cascade` 에 의해 삭제되는 것이기 때문에 `envers 이력에 남지 않는다.`
- user delete 후 `user_some_data2_h` 에 이력이 없는 것을 확인


### HHH015007: Illegal argument on static metamodel field injection
- spring boot 3.2.x 부터 발생
- error log 발생되지만 동작에는 문제없음
- https://hibernate.atlassian.net/browse/HHH-17612
- hibernate 6.6.8(springboot 3.4.3) error log 미노출 확인
```
o.h.metamodel.internal.MetadataContext   : HHH015007: Illegal argument on static metamodel field injection : org.hibernate.envers.DefaultRevisionEntity_#class_; expected type :  org.hibernate.metamodel.model.domain.internal.EntityTypeImpl; encountered type : jakarta.persistence.metamodel.MappedSuperclassType
```

### 참고
https://sehajyang.github.io/2020/04/15/springboot-envers-logging-for-revision/
