# spring-GraalVM

## GraalVM Native Image

- **AOT 컴파일**: `main` 진입점부터 정적 분석해 플랫폼별 단일 실행 파일 생성. JVM/JIT 불필요.
- **Closed-world**: 클래스패스와 빈 정의가 **빌드 타임에 고정**. 런타임 동적 클래스 로딩·동적 빈 생성 불가. `@Profile`, `@ConditionalOnProperty` 등 동적 조건은 빌드 시점 값으로 결정됨.
- **장점**: 밀리초대 기동, 낮은 메모리, JVM 미포함 소형 이미지 → 서버리스·컨테이너 적합.

### 주의사항

- **Reachability metadata 필요**: 리플렉션·리소스·프록시·직렬화·JNI 는 정적 분석으로 못 잡음 → 힌트 필요. Spring AOT + Hibernate 가 대부분 자동 생성(`reflect-config.json` 등). 서드파티/커스텀 리플렉션은 `RuntimeHintsRegistrar` 또는 `@RegisterReflectionForBinding` 로 직접 등록.
- **빌드 시간 김**: 네이티브 빌드는 수 분 소요(AOT 특성). 반복 개발은 JVM 모드 권장.
- **런타임 != JVM**: 프로파일·환경변수로 빈 구성을 바꾸는 패턴은 빌드 시점에 고정되므로 동작이 다를 수 있음.
- **디버깅**: 네이티브에서 문제 발생 시 `-Pnative` 없이 JVM 모드로 먼저 재현.


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

