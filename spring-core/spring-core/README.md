## Spring bean

Component Scan - Auto Configuration

#### Component Scan
`@Component` 어노테이션이 붙은 클래스를 Spring bean 으로 등록한다. 
```
@Service, @Repository, @Controller 모두 @Component 를 가지고 있다.
```

위 등록의 시작지점은 `@ComponentScan` 의 basePackage에 의해 결정된다.
basePackage 를 지정하지 않는다면 `@ComponentScan` 이 지정된 패키지를 포함해서 시작지점이 된다.

보통 Springboot 의 경우 main 클래스의 `@SpringBootApplication` 에 붙어있다.


#### Auto Configuration
Auto Configuration 는 Springboot 가 제공하는 클래스를 Spring bean 으로 등록한다.
`@EnableAutoConfiguration` 에 의해 동작하고 springboot 의 경우 main 클래스에 붙어있다.

`@EnableAutoConfiguration` 에 의해 등록되는 Spring bean 은 아래 경로에서 확인할 수 있다.
```
spring-boot-autuconfigure-[version].jar/META-INF/spring.factories
```
image.png


#### 순서
`Component Scan -> Auto Configuration` 의 순서로 Spring bean 을 등록한다.
Component Scan 에 의해 등록되는 bean 이 auto configuration 에 등록되어있는 경우 충돌이 발생할 수 있다.
