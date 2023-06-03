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

### 참고
https://www.baeldung.com/java-compress-and-uncompress