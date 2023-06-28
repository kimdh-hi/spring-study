## Spring MVC


### pageable page 1부터 시작도록 설정
```yaml
spring:
  data:
    web:
      pageable:
        one-indexed-parameters: true
```

- `page` 를 0으로 받든, 1로 받든 page=0으로 처리됨
- but, `Page` 타입 반환시 페이지는 0으로 처리됨

응답시 페이지 여전히 0 문제
- 응답시 CustomResponse 로 page+1 처리
- @JsonComponent 로 page 직렬화 시 page+1 처리

---

### @InitBinder
@Valid 어노테이션으로 검증하기 전에 수행될 메서드를 지정할 수 있다. <br/>
기본적으로 모든 요청에 대해 동작하기 때문에 특정 dto 에 적용하고 싶다면 `@initBinder("xxxRequestDto)` 로 지정 가능하다. <br/>


### 참고
https://www.baeldung.com/java-compress-and-uncompress
