Hibernate 6.x (data jpa 3.x)

`distinct` 
- hibernate6 이전 컬렉션 조인시 자식 컬렉션의 크기만큼 row 가 늘어나는 경우 `distinct` 키워드로 중복을 제거
- hibernate6 부터 위와 같은 케이스에 대해 `distinct` 자동 적용

https://github.com/hibernate/hibernate-orm/blob/6.0/migration-guide.adoc#distinct

```
DISTINCT
Starting with Hibernate ORM 6 it is no longer necessary to use distinct in JPQL and HQL to filter out the same parent entity references when join fetching a child collection. The returning duplicates of entities are now always filtered by Hibernate.
```

---

`logging`
- `level.org.hibernate.orm.jdbc.bind` -> `org.hibernate.orm.jdbc.bind`

---

hibernate 6.x @Enumerated 관련 이슈

```
Datatype for enums
Hibernate 6.1 changed the implicit SQL datatype for mapping enums from TINYINT to SMALLINT to account for Java supporting up to 32K enum entries which would overflow a TINYINT. However, almost no one is developing enums with that many entries. Starting in 6.2, the choice of implicit SQL datatype for storing enums is sensitive to the number of entries defined on the enum class. Enums with more than 128 entries are stored as SMALLINT implicitly, otherwise TINYINT is used.

NOTE
On MySQL, enums are now stored using the ENUM datatype by default
```

```
mysql 의 경우 enum 사용시 ENUM 데이터타입 사용

@Enumerated 를 사용해서 varchar 타입의 컬럼을 생성하는 것 현재 불가능한 듯 (EnumConverter 로 우회 가능)
- 2023/11/20 관련 이슈가 논의되고 있음
https://hibernate.atlassian.net/browse/HHH-17180
```

https://docs.jboss.org/hibernate/orm/6.2/migration-guide/migration-guide.html#ddl-implicit-datatype-enum

https://discourse.hibernate.org/t/hibernate-6-cannot-persist-enum-as-ordinal-in-varchar-column/7775