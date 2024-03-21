

Spring aop cglib
- spring 3.2 > CGLIB 내장
- spring 4.0 > CGLIB 기본 생성자 필수 문제 해결
  - `objenesis` 기본 생성자 없이 객체 생성을 위한 라이브러리
- spring 4.0 > CGLIB 생성자 두 번 호출 문제 해결 (`objenesis`)
- spring boot 2.0 > CGLIB default
  - 구체 클래스 DI 가능
  - spring boot 는 AOP 적용시 default 로 CGLIB 사용 (`proxyTargetClass=true`)

@EnableAspectJAutoProxy
- proxy 기반 aop 동작을 위한 proxy 생성시 인터페이스를 구현할 것인지, 구체 클래스를 상속할 것인지에 대한 옵션 지정
  - `@EnableAspectJAutoProxy(proxyTargetClass = false)`
  - proxyTargetClass (default=false)
- *spring boot 사용시 proxyTargetClass 는 true 가 기본값으로 처리된다.*
  - spring boot 는 cglib 라이브러리가 안정화되었다고 판단.

```
spring 이 지정한 proxyTargetClass=false 기본값을 그대로 사용하고 싶은 경우

spring.aop.proxy-target-class: false
```

---

```kotlin
@Before("execution(* com.toy.springaop.controller.*.*(..))")
@TestAnnotation
fun testAspect() { }
```
- testAspect 가 적용되는 곳에 @TestAnnotation 이 붙기를 의도 (실패)

```java
//GPT code...
@Aspect
@Component
public class TransactionAspect {
    @Pointcut(“execution(* com.example.service.*.*get*(..)) ” +
              “|| execution(* com.example.service.*.*find*(..)) ” +
              “|| execution(* com.example.service.*.*check*(..))“)
    public void readOnlyMethods() {}
  
    @Pointcut(“execution(* com.example.service.*.*(..)) ” +
              “&& !execution(* com.example.service.*.*get*(..)) ” +
              “&& !execution(* com.example.service.*.*find*(..)) ” +
              “&& !execution(* com.example.service.*.*check*(..))“)
    public void readWriteMethods() {}
  
    @Before(“readOnlyMethods()“)
    @Transactional(readOnly = true)
    public void applyReadOnlyTransaction() {
        // Empty method, transactional annotation is enough to handle transaction
    }
    @Before(“readWriteMethods()“)
    @Transactional(readOnly = false)
    public void applyReadWriteTransaction() {
        // Empty method, transactional annotation is enough to handle transaction
    }
}
```