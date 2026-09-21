# Konsist

## 유사 툴 비교

- **Konsist = Kotlin 소스 규칙 검사:** 이름, 패키지, 애노테이션, 함수, 프로퍼티, import 같은 Kotlin 코드 형태 검증
- **Detekt = Kotlin 코드 품질 검사:** 복잡도, 잠재적 버그, 코드 스멜, 스타일 같은 일반 품질 문제 검출
- **ArchUnit = JVM 아키텍처 검사:** 컴파일된 클래스의 참조, 호출, 순환 의존성 같은 바이트코드 관계 검증

## Konsist 특징

- **분석 대상:** `.kt` 소스 파일
- **주요 단위:** 클래스, 함수, 프로퍼티, 애노테이션, modifier, import, 패키지
- **Kotlin 인식:** `data class`, 확장 함수, top-level 선언 등 Kotlin 고유 구조
- **범위 선택:** `scopeFromProduction()`, `scopeFromTest()`, `scopeFromProject()`
- **검증 방식:** JUnit 또는 Kotest에서 실행하는 일반 테스트
- **아키텍처 검사:** 패키지 기반 Layer의 허용·금지·필수 의존성
- **지원 대상:** Kotlin, Android, Spring, Kotlin Multiplatform 프로젝트

## 도구별 핵심 차이

- **주목적:** Konsist는 팀 고유 구조 규칙 / Detekt는 일반 코드 품질 / ArchUnit은 JVM 아키텍처 규칙
- **분석 대상:** Konsist는 Kotlin 소스 declaration / Detekt는 Kotlin 소스와 선택적 타입 정보 / ArchUnit은 컴파일된 JVM 바이트코드
- **규칙 작성:** Konsist는 Kotlin 테스트 코드 / Detekt는 기본 RuleSet과 YAML 설정 / ArchUnit은 테스트 DSL
- **대표 검증:** Konsist는 이름·패키지·애노테이션·선언 구조 / Detekt는 복잡도·코드 스멜·잠재적 버그 / ArchUnit은 호출·순환 의존성·Layer·Slice
- **결과 관리:** Konsist와 ArchUnit은 테스트 실패 / Detekt는 빌드 실패·리포트·Baseline
- **플랫폼 범위:** Konsist와 Detekt는 Kotlin 중심 / ArchUnit은 JVM 중심
- **선택 기준:** 팀 구조 규칙은 Konsist / 코드 품질 자동 검사는 Detekt / 대규모 JVM 의존성 통제는 ArchUnit

## 이 프로젝트의 검증 규칙

- **Application:** `@SpringBootApplication` 클래스의 `Application` 접미사
- **Controller:** `@RestController` 클래스의 `Controller` 접미사와 `controller` 패키지
- **Service:** `@Service` 클래스의 `Service` 접미사와 `service` 패키지
- **의존 방향:** Controller Layer에서 Service Layer로 향하는 실제 의존성
- **패키지 경로:** package 선언과 실제 디렉터리 경로 일치
- **import 규칙:** wildcard import 금지
- **금지 API:** `java.util.logging` import 금지
- **테스트 코드:** `src/test/kotlin/com/toy/konsist/KonsistTest.kt`
- **실행 명령:** `./gradlew test`

## Detekt와 겹치는 영역

- **Konsist로 가능:** import, 금지 API, 패키지 경로, 이름, 선언 순서처럼 직접 표현 가능한 팀 규칙
- **Detekt가 적합:** 순환 복잡도, 긴 함수, 과도한 중첩, 잠재적 버그처럼 준비된 RuleSet과 임계값이 필요한 검사
- **구분 기준:** 원하는 규칙을 테스트 코드로 직접 정의하면 Konsist / 일반 코드 스멜을 설정으로 관리하면 Detekt

## 선택 결론

- **현재 선택:** Kotlin 명명·패키지·애노테이션 규칙이 핵심이므로 Konsist 사용
- **Detekt 추가 시점:** 복잡도·코드 스멜·잠재적 버그를 공통 설정과 리포트로 관리하는 시점
- **ArchUnit 추가 시점:** 멀티 모듈 순환 참조와 메서드·필드 접근 통제가 필요해지는 시점
- **동시 사용:** 구조 규칙·코드 품질·JVM 아키텍처의 검사 책임이 분리되는 시점

## 참고

- **Konsist:** [저장소](https://github.com/LemonAppDev/konsist) · [문서](https://docs.konsist.lemonappdev.com/)
- **Detekt:** [문서](https://detekt.dev/docs/intro/)
- **ArchUnit:** [문서](https://www.archunit.org/userguide/html/000_Index.html)
