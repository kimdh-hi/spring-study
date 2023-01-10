
## GraphQL

기존 api 방식의 경우 클라이언트에서 데이터를 이용해 한 개 화면을 그리기 위해서는 여러 번의 API 호출이 필요할 수 있다.<br/>
클라이언트는 데이터를 받아오기 위해 여러 번 api 를 호출하고 해당 데이터들을 조합해서 화면을 그리게 된다.<br/>
여러 번 api 를 호출해서 데이터를 받아올 때 어쩌면 사용하지 않는 불필요한 데이터가 많이 포함되어 있을 수도 있다.<br/>

`GraphQL` 은 클라이언트가 필요한 데이터만을 질의할 수 있도록 한다.
클라이언트는 필요한 데이터에 대한 쿼리를 구성하고 GraphQL 은 해당 쿼리를 통해 서버에 데이터를 요청하고 응답한다.

`user` 의 id에  name과 username 을 가져오는 쿼리
```
query User($id: Int) {
    user(id: $id) {
        name
        username
    }
} 
```

추가로 role 을 가져오고 싶다면?
```
query User($id: Int) {
    user(id: $id) {
        name
        username
        role
    }
}
```

---

### 참고
https://graphql-kr.github.io/learn/queries/
https://danawalab.github.io/spring/2022/06/06/Spring-for-GraphQL.html