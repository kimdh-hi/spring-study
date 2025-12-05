# Traditional Deployment

springboot 는 servlet container 에 배포하는 방식 (traditional deployment) 와 임베디드 서버를 사용하는 현대적 배포 모두 지원한다.

```
단, spring webflux 의 경우 기본적으로 reactor netty 임베디드 서버가 사용되므로 war 배포를 지원하지 않는다.
```

## Deployable WAR

1. SpringBootServletInitializer 상속
- 외장 tomcat 과 같은 외부 서블릿 컨테이너(was)가 configure 를 통해 application context 를 구성하는 등의 초기화 지점
- main 클래스가 아닌 별도 클래스에서 SpringBootServletInitializer 를 상속해도 문제없음.
```kotlin
@SpringBootApplication
class MyApplication : SpringBootServletInitializer() {

	override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
		return application.sources(MyApplication::class.java)
	}

}

fun main(args: Array<String>) {
	runApplication<MyApplication>(*args)
}
```

2. war 생성 위한 빌드 설정

```gradle.kts
plugins {
  kotlin("jvm") version "2.2.21"
  kotlin("plugin.spring") version "2.2.21"
  id("org.springframework.boot") version "4.0.0"
  id("io.spring.dependency-management") version "1.1.7"
  //...
  
  war
}
```

3. 임베디드 서블릿 컨테이너 충돌 방지 (외부 서블릿 컨테이너 사용하는 경우)
- `spring-boot-starter-tomcat` 의존성을 provided 로 설정하여 배포시 포함되지 않도록 하기 위함
- `providedRuntime` 권장 (`compileOnly` 의 경우 test claapath 에 포함되지 않아 테스트 실패 등 문제 발생 가능)
```
dependencies {
  providedRuntime("org.springframework.boot:spring-boot-starter-tomcat-runtime")
}
```


---

## reference
- https://docs.spring.io/spring-boot/how-to/deployment/traditional-deployment.html