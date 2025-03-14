## spring kotlin

#### value class 사용시 이슈
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
