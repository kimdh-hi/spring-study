# spring-GraalVM


## GraalVM Native Image

- 일반적인 JVM 애플리케이션의 경우 `.jar` 를 JVM 실행 시점에 로드하고, JIT(Just-In-Time) 컴파일러가 런타임에 바이트코드를 기계어로 변환
- Native Image 는 AOT(Ahead-Of-Time) 컴파일을 통해 빌드 시점에 앱 전체를 독립 실행형 네이티브 바이너리로 생성
  - 이 바이너리는 JVM 없이 OS 위에서 직접 실행됨
- **AOT 컴파일**: `main` 진입점부터 정적 분석해 플랫폼별 단일 실행 파일 생성. JVM/JIT 불필요.
  - 결과물은 완전한 플랫폼 종속(OS 종속, platform-specific) 실행 파일이므로 구동 위해 JVM 을 함께 배포할 필요없음. 
- **Closed-world**
  - 빌드 시점에 실행 가능한 모든 코드 경롤를 정적 분석
  - 실제 도달 가능한 (reachable) 코드만 바이너리에 포함 (나머지는 제거)
  - 도달할 수 없는 코드는 제어되기 때문에 리플랙션, 리소스, 직렬화, 동적 프록시 등에 대해 명시적으로 알려줘야 함. 
- **장점**: 밀리초대 기동, 낮은 메모리, JVM 미포함 소형 이미지 → 서버리스·컨테이너 적합.

## GraalVM Native Image Trade-off

- 전형적인 rest + jpa 기준 
  - 기동시간
    - 일반 jvm: 2.8초
    - native-image: 0.065초
  - 메모리
    - 일반 jvm: 280MB
    - native-image: 62MB
  - 빌드 시간
    - 일반 jvm: 15초
    - native-image: 4~5분
  - 처리량
    - 일반 jvm: 12,500건
    - native-image: 9,800건
- 기동시간, 메모리 등에 이점이 있지만 처리량이 감소하는 트레이드 오프
- 처리량 감소 이유
  - JVM(JIT) 의 경우 처음 실행시 지연이 발생 (JIT 특성, 런타임에 바이트 코드로 변환)
  - JVM 이 계속 실행되면서 프로파일링 통해 자주 실행되는 코드 (핫패스) 를 기계어로 컴파일 등 최적화 ==> warm-up
  - 즉, 시간이 지날수록, warm-up 이 될수록 처리량이 높아짐
  - native-image(AOT) 의 경우 빌드 시점에 모든 컴파일을 완료하고, 실행중 컴파일러가 존재하지 않음.
- JIT 최적화의 부재로 native-image 는 실행 즉시 안정적인 성능을 내지만, 장시간 실행시 JIT의 프로파일 기반 최적화 통해 일반적인 JVM 애플리케이션이 더 나은 처리량을 낼 수 있음 

## Spring Native Image

- sping 은 런타임 동적 기능에 의존
  - reflection: 빈 생성, DI 등..
  - dynamic proxy: AOP..
  - classpath scanning: @ComponentScan 으로 런타임에 컴포넌트 탐색
  - 프록시 클래스 런타임 생성: CGLIB 바이트코드 조작
  - 리소스 로딩: 설정파일, messages.properties(메세지 번들) 등 런타임 조회
- native-image closed world 로 인해 리플랙셔, 런타임 클래스 생성 등은 기본적으로 동작 불가
- 별도 설정 메타데이터 (reflect-config.json, resource-config.json, proxy-config.json..) 을 명시적으로 알려줘야 함.
- spring native 프로젝트 통합 (spring6/springboot3 (2022/11))
  - 빌드 시점 AOT 처리 (Build-time AOT transformation)
  - spring context 를 빌드 시점에 미리 처리
  - 런타임 작업을 컴파일 타입으로 옮김
- 프레임워크 차원에서 대부분의 런타임 기능을 빌드시점 처리 등을 지원하지만, 직접 리플랙션 또는 리소스 로딩 사용시 직접 힌트 커스텀 힌트 등록 필요

```java
@Configuration
@ImportRuntimeHints(MyRuntimeHints.class)
public class AppConfig {
}

public class MyRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(MyReflectiveClass.class,
            MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
            MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.resources().registerPattern("templates/*.html");
    }
}
```

### JVM AOT enabled
- aot 모드는 활성화하는 것은 native-image 만을 위한 옵션 아님
  - https://docs.spring.io/spring-framework/reference/core/aot.html#aot.running
  - JVM 에서도 설정 가능
- JVM 상에서도 aot 활성화 가능하고 일정 부분 기동시간에 이점이 있음
  - 단, 권장한다는 문구는 공식문서상 발견못함.
  - aot 적용에 따라 주의사항이 많아지고, 서버 기동은 최초 한 번만 치르는 비용이므로 비교적 중요하지 않다는 판단


### 주의사항

- **Reachability metadata 필요**: 리플렉션·리소스·프록시·직렬화·JNI 는 정적 분석으로 못 잡음 → 힌트 필요. Spring AOT + Hibernate 가 대부분 자동 생성(`reflect-config.json` 등). 서드파티/커스텀 리플렉션은 `RuntimeHintsRegistrar` 또는 `@RegisterReflectionForBinding` 로 직접 등록.
- **빌드 시간 김**: 네이티브 빌드는 수 분 소요(AOT 특성). 반복 개발은 JVM 모드 권장.
- **런타임 != JVM**: 프로파일·환경변수로 빈 구성을 바꾸는 패턴은 빌드 시점에 고정되므로 동작이 다를 수 있음.
- **디버깅**: 네이티브에서 문제 발생 시 `-Pnative` 없이 JVM 모드로 먼저 재현.

### 주요 컴포넌트 적용 시

추가 부담의 대부분은 **"직렬화되는 내 DTO에 바인딩 힌트(`@RegisterReflectionForBinding`)"** 하나로 수렴. 검증은 반드시 `nativeTest` 또는 실제 네이티브 실행으로 (리플렉션 누락은 런타임에만 터짐).

| 컴포넌트 | 신경 쓸 점 |
|---|---|
| Virtual Thread | 없음. GraalVM 21+ 완전 지원, 순수 런타임 기능 |
| Redis | 값 직렬화 타입에 바인딩 힌트. JDK 직렬화 대신 **JSON serializer 권장** |
| Kafka | JSON/Avro 페이로드 DTO에 바인딩 힌트. Streams 는 리플렉션 많아 추가 가능성 |
| Cache / HTTP 클라이언트 | 직렬화·응답 바디 타입에 바인딩 힌트. `@HttpExchange` 는 프록시 힌트(선언분 자동) |
| Security / Actuator / Flyway | 공식 지원, Spring AOT 자동 힌트 |
| `@Profile`·`@ConditionalOnProperty` | 빌드타임 값으로 고정 → 런타임 전환 불가 |

- 유명 라이브러리는 **GraalVM reachability-metadata 저장소**(native-build-tools 기본 참조)에 힌트가 올라와 있어 손 안 대도 되는 경우가 많음.

`nativeCompile` 로 로컬 네이티브 빌드 시 **GraalVM for JDK 25** 필요:

```bash
sdk install java 25-graal     # SDKMAN 예시
sdk use java 25-graal
```

- `bootBuildImage` 는 Docker 만 있으면 됨 (buildpack 이 컨테이너 안에서 네이티브 빌드).

## 실행

### 1. JVM 모드 (기준선)

```bash
./gradlew bootRun
```

### 2. 네이티브 실행 파일 (로컬 GraalVM 필요)

```bash
./gradlew nativeCompile
./build/native/nativeCompile/spring-GraalVM
```

### 3. 네이티브 컨테이너 이미지 (Docker 만 필요)

```bash
./gradlew bootBuildImage
docker run --rm -p 8080:8080 --network host spring-graalvm:0.0.1-SNAPSHOT
```

### 4. 네이티브 테스트

```bash
./gradlew nativeTest
```

## Reference

- https://docs.spring.io/spring-boot/reference/packaging/native-image/introducing-graalvm-native-images.html
- 
