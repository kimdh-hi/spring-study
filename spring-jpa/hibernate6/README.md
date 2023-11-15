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