## Executable Jar


without spirngboot deploy
- tomcat 설치
- war build
- war -> was(deploy)
- was 실행

containerless web application architecture
- https://github.com/spring-projects/spring-framework/issues/14521
- container == tomcat (servlet container)

war 
- 내부적으로 외부 라이브러리에 대한 jar 를 포함 (WEB-INF/lib/**.jar)

jar
- jar 내부에 jar 를 포함하는 (nested jar) 를 로드하는 표준적인 방식이 없음.
- jar 내부에 jar 를 처리하기 위해 fat jar(uber jar) 등의 방식이 사용됐었음. (한계가 있음)

far jar (uber jar, sharded jar)
- 중첩 jar 를 포함할 수 없는 한계를 극복하기 위함
- 외부 jar 를 풀어서 획득한 class 파일들을 함께 jar 로 패키징
- 모든 외부 라이브러리 클래스 파일이 포함된 무거운 jar (far jar) 탄생
- fat jar 는 모든 외부 라이브러리 클래스 파일이 그래도 포함되므로 클래스 패스 충돌 등 문제 발생 가능

executable jar (nested jar)
- springboot 는 jar 중첩 (nested jar) 패키징 지원
- BOOT-INF/lib 외부 라이브러리 jar 위치
- 단, 실행시 별도 loader 필요 (spring-boot-loader)

```
example.jar
 |
 +-META-INF
 |  +-MANIFEST.MF
 +-org
 |  +-springframework
 |     +-boot
 |        +-loader
 |           +-<spring boot loader classes>
 +-BOOT-INF
    +-classes // application code
    |  +-org
    |     +-exmaple 
    |        +-SpringApplication.class
    +-lib // external libraries
       +-lib1.jar
       +-lib2.jar
    +-classpath.idx
    +-layers.idx
```

META-INF.MANIFEST.MF
- java -jar 시 먼저 참조되어 실행에 필요한 메타데이터를 제공
- jar 실행시 진입점 관련 설정 제공 (Main-Class, Start-Class 등)

```
Manifest-Version: 1.0
Main-Class: org.springframework.boot.loader.launch.JarLauncher
Start-Class: com.rsupport.xr.api.TestApplicationKt
Spring-Boot-Version: 3.5.7
Spring-Boot-Classes: BOOT-INF/classes/
Spring-Boot-Lib: BOOT-INF/lib/
Spring-Boot-Classpath-Index: BOOT-INF/classpath.idx
Spring-Boot-Layers-Index: BOOT-INF/layers.idx
Build-Jdk-Spec: 21
Implementation-Title: test
Implementation-Version: 0.55.2.0
```

executable jar 실행
- Main-Class 실행 (JarLauncher)
- application class(BOOT-INF/classes), 중첩jar(BOOT-INF/lib) 에 대한 classLoader 구성
- BOOT-INF/classess, BOOT-INF/lib/*.jar 스캔
- classpath.idx 에 정의된 순서로 classpath 에 추가
- MANIFEST.MF 에 명시된 Start-Class 실행

---

## reference
- https://docs.spring.io/spring-boot/specification/executable-jar/nested-jars.html