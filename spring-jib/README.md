# Spring Boot Jib

- Dockerfile 없는 Jib 컨테이너화 샘플

## Jib

- Java 애플리케이션을 Docker 데몬 없이 Docker·OCI 이미지로 빌드하는 Google의 Maven·Gradle 플러그인
- 범선 뱃머리의 삼각돛을 뜻하는 `jib`에서 유래 (이름은 약자가 아님)
- OCI(Open Container Initiative)는 컨테이너 이미지 포맷과 레지스트리 API의 업계 표준 규격

## 컨테이너 실행

- 로컬 Docker 이미지 생성 명령어 `./gradlew jibDockerBuild`
- 컨테이너 실행 명령어 `docker run --rm -p 8080:8080 spring-jib:latest`
- Docker 데몬 없는 이미지 tar 생성 명령어 `./gradlew jibBuildTar`
- 이미지 tar 로드 명령어 `docker load --input build/jib-image.tar`
- 레지스트리 푸시 명령어 `./gradlew jib --image=<registry>/<repository>:<tag>`

## Jib 동작 방식

- Docker 데몬 없이 Gradle 프로세스가 OCI 이미지를 직접 조립
- OCI(Open Container Initiative)는 컨테이너 이미지 포맷과 레지스트리 API의 업계 표준 규격
- 이미지 구성은 파일시스템 레이어 tar와 메타데이터 JSON(entrypoint, 환경변수, 포트)의 조합
- 표준 준수로 Docker, containerd, Kubernetes 등 모든 런타임에서 동일하게 실행
- base image는 매니페스트만 조회하고 레이어는 레지스트리 간 mount로 재사용
- fat jar 생성 없이 의존성 / 스냅샷 의존성 / 리소스 / 클래스를 개별 레이어로 분리
- 코드 변경 시 클래스 레이어만 재빌드·재전송

## Dockerfile 대비 차이

- Docker 데몬 불필요로 CI에서 DinD·privileged 설정 제거
- 레이어 분리가 기본 제공되어 캐시 최적화 수작업 불필요
- 이미지 설정을 `build.gradle.kts` 한 곳에서 관리
- 타임스탬프 고정으로 동일 입력 시 동일 다이제스트 보장
- `apt-get`, 멀티스테이지 빌드, 커스텀 entrypoint 스크립트는 불가

## 설정 항목

| 항목 | 값 | 목적 |
| --- | --- | --- |
| `from.image` | `eclipse-temurin:25-jre` | 기본값 distroless는 셸이 없어 디버깅 불편 |
| `to.tags` | `version` | `latest`와 버전 태그 동시 부여 |
| `container.ports` | `8080` | `ExposedPorts` 메타데이터 |
| `container.user` | `1000:1000` | non-root 실행 |
| `container.jvmFlags` | `-XX:MaxRAMPercentage=75` | 컨테이너 메모리 한도 기준 힙 산정 |
| `container.creationTime` | `USE_CURRENT_TIMESTAMP` | 기본값은 에포크라 생성일이 1970으로 표시 |

## 기존 Dockerfile

```dockerfile
FROM bellsoft/liberica-openjre-debian:25-cds AS builder
WORKDIR /builder
COPY build/libs/*.jar application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

FROM bellsoft/liberica-openjre-debian:25-cds
WORKDIR /application
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

ENTRYPOINT ["java", "-jar", "application.jar"]
```

- Spring Boot `jarmode=tools`의 `extract --layers`로 fat jar를 4개 레이어로 분해하는 멀티스테이지 방식
- 레이어 구성은 dependencies / spring-boot-loader / snapshot-dependencies / application

## Jib 전환 시 제한

- 빌드 중 임의 명령 실행 미지원 — 공식 FAQ의 "We do not recommend or support running commands as part of the build"
- CDS·AOT 캐시 생성은 `RUN java -XX:ArchiveClassesAtExit=...` 트레이닝 런이 필요해 불가
- CDS 아카이브는 생성 시점과 런타임의 클래스패스 문자열이 일치해야 유효하므로 `extraDirectories` 우회도 비현실적
- `java -jar` 실행 방식 미지원 — 공식 FAQ의 "Jib does not natively support creating an image that runs a JAR through `java -jar runnable.jar`"
- `spring-boot-loader` 레이어와 `JarLauncher` 제거로 중첩 jar URL·`PropertiesLauncher` 의존 구성은 동작 변경
- 멀티스테이지 빌드, `apt-get`, 파일 권한 조작 불가 — 커스텀 base image로만 우회
- `HEALTHCHECK` 설정 불가 — OCI 이미지 스펙에 없는 Docker 전용 필드
- GraalVM 네이티브 이미지, CRaC 체크포인트 불가

## Jib 전환 시 장점

- `-cds` 태그 base image의 JDK 클래스 CDS 아카이브는 `from.image` 지정으로 그대로 유지
- 레이어 분리가 더 세밀 — 리소스와 클래스를 추가 분리해 코드 변경 시 전송량 감소
- `./gradlew bootJar` 후 `docker build` 2단계가 `./gradlew jib` 1단계로 축소
- fat jar 생성 없이 클래스·의존성을 직접 배치해 빌드 시간 단축
- Docker 데몬 불필요로 CI에서 DinD·privileged 설정 제거
- 이미지 설정을 `build.gradle.kts` 한 곳에서 관리
- 타임스탬프 고정으로 동일 입력 시 동일 다이제스트 보장
- `container.user` 설정으로 non-root 실행 — 기존 Dockerfile은 `USER` 지시어 부재로 root 실행

## 선택 기준

- CDS·AOT 캐시로 기동 시간 단축이 목적이면 Dockerfile 유지
- 레이어 캐싱과 빌드 단순화가 목적이면 Jib이 상위 호환
- Dockerfile 없이 CDS가 필요하면 `./gradlew bootBuildImage`의 Paketo 빌드팩 검토

## 주의 사항

- `from.platforms` 멀티플랫폼은 레지스트리 푸시(`jib`)만 지원하며 `jibDockerBuild`·`jibBuildTar`는 실패
- `creationTime` 기본값 변경 시 재현 가능한 다이제스트 포기
- macOS에서 `docker-credential-osxkeychain` 경고 발생 시 익명 pull로 폴백되어 빌드에는 무영향

## References

- [Jib GitHub](https://github.com/GoogleContainerTools/jib)
- [Jib Gradle 플러그인 설정 레퍼런스](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin)
- [Jib FAQ](https://github.com/GoogleContainerTools/jib/blob/master/docs/faq.md)
- [OCI Image Format Specification](https://github.com/opencontainers/image-spec)
