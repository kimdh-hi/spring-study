# spring-modulith

## @ApplicationModuleListener

- async, new transaction, after commit 조합 meta annotation

```
@Async
@Transactional(propagation = Propagation.REQUIRES_NEW)
@TransactionalEventListener
@Documented
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationModuleListener
```

## @ApplicationModuleTest

- 해당 테스트 클래스가 속한 패키지를 포함하는 모듈 내 bean 만 scan 대상으로 취급
- ApplicationModuleTest 의 verifyAutomatically 설정시 타모듈 접근, 순환참조 등 검증
  - default: true

## Modularity test
- ModularityTests.kt (sample)
- 특정 모듈에 대한 테스트가 존재한다면 ApplicationModuleListener 에 의해 모듈 간 참조 등 검증됨.
  - 단, 해당 모듈이하 테스트가 없는 경우 테스트 불가.
  - 검증누락 안전장치로써 `modules.verify()` 사용 가능
- 모듈 간 의존 문서화 기능 제공 (console, puml, adoc..)


## 참고

- https://docs.spring.io/spring-modulith/reference/index.html
- https://docs.spring.io/spring-modulith/reference/fundamentals.html
- https://docs.spring.io/spring-modulith/reference/testing.html
