# Sentry 정리

- **Sentry** = 애플리케이션 에러/예외 트래킹 + 성능 모니터링(APM) 플랫폼
- 로그·메트릭 수집기와 달리 **에러를 이슈 단위로 집계·그룹핑·알림**하는 데 특화
- 30여 개 언어/프레임워크 SDK 지원, SaaS(sentry.io) 또는 self-hosted로 운영

## 핵심 개념

| 용어 | 의미 |
|------|------|
| Event | 캡처된 단일 발생(에러·트랜잭션 등) |
| Issue | 같은 근본 원인의 Event를 fingerprint로 묶은 그룹 |
| DSN | 프로젝트 식별·전송 대상 키(Client Key) |
| Release | 배포 버전 식별자, 이벤트에 부착 |
| Environment | production/staging 등 실행 환경 구분 |
| Transaction | 성능 모니터링의 요청 단위(스팬 트리 루트) |
| Breadcrumb | 이벤트 직전 발생 이력 타임라인 |

## Issues — 에러 트래킹 핵심

- **자동 캡처** — 미처리 예외·HTTP 5xx·수동 `captureException`을 이벤트로 수집
- **지문(fingerprint) 그룹핑** — 스택트레이스·예외 타입 기반으로 동일 원인 이벤트를 이슈 1건으로 dedup, 커스텀 규칙 지원
- **리치 스택트레이스** — 소스 컨텍스트(주변 코드 라인), 프레임별 로컬 변수, in-app/library 프레임 구분
- **breadcrumbs** — 에러 직전 이벤트 타임라인(로그·HTTP·쿼리·클릭 등) 자동 기록
- **컨텍스트/태그** — user, request, environment, release, 커스텀 tag → 이슈 검색·필터·집계 축
- **이슈 상태 워크플로** — resolve / ignore / archive, **regression 자동 감지**(해결된 이슈 재발생 시 재오픈)
- **빈도·영향도** — events / users affected 카운트, 시간대별 추이, 최초·최근 발생
- **Ownership rules** — 코드 경로별 담당자 자동 배정, 이슈 assign

## Alerts — 알림·에스컬레이션

- **Issue alerts** — 신규 이슈·재발생·빈도 임계치 초과 시 트리거
- **Metric alerts** — 에러율·트랜잭션 지표 임계치 기반
- **채널** — Slack, 메일, PagerDuty, Opsgenie, webhook, MS Teams
- **에스컬레이션 정책** — 조건·라우팅·throttle(중복 억제) 규칙 구성

## Releases — 릴리스 헬스

- **릴리스 태깅** — 배포 버전을 이벤트에 부착, 릴리스별 에러 추이 비교
- **crash-free rate** — 릴리스별 세션/유저 무장애 비율
- **regression 추적** — 특정 릴리스에서 새로 생긴 이슈 식별
- **suspect commit** — git 연동으로 에러를 유발한 커밋·작성자 추정, PR/커밋 링크
- **deploy 이벤트** — 배포 시점 마킹 → 배포 전후 에러 변화 상관

## Performance · Tracing · Insights

- **트랜잭션/스팬** — 요청 단위 분산 트레이싱, span별 지연 분해
- **Performance Issues 자동 탐지** — N+1 쿼리, 느린 DB 쿼리, 연속 HTTP 호출 등 안티패턴 자동 검출
- **Insights** — 백엔드 빌딩블록별 집계 대시보드
  - Queries(DB), Caches, Queues, Outbound HTTP(외부 요청), Web Vitals
- **span 파생 메트릭** — span attribute 기반 집계(고정 카디널리티 커스텀 메트릭은 아님)
- **trace view** — 서비스 경계를 넘는 요청 흐름 + 연관 에러·로그 연결

## Profiling

- **Continuous Profiling** — 백엔드 코드 레벨 CPU/시간 프로파일, 함수별 hot path
- 트랜잭션/스팬과 연결 — 느린 span의 실제 코드 원인 추적

## Cron · Uptime 모니터링

- **Cron/Check-in** — 스케줄 작업의 실행 누락·실패·지연 감지(하트비트)
- **Uptime** — URL 1분 주기 헬스체크, non-200·타임아웃·DNS 실패 알림, 실패를 트레이스와 연결

## Logs

- **Structured Logs**(2025-09 GA) — trace 연결형 구조화 로그, 에러·스팬과 단일 워크플로로 통합
- 매 로그에 trace_id/span_id 부착 → 요청 흐름상 위치 파악
- 공식 포지셔닝 — 벌크 로그 저장소 대체가 아닌 **디버깅 컨텍스트 보강** 목적

## Seer — AI 디버깅 (유료)

- **Issue Summary** — 이슈 메타데이터에서 무엇이·왜 잘못됐는지 요약
- **Root Cause Analysis** — 트레이스·로그를 AI로 분석해 근본 원인 추정(발표 기준 정확도 높음)
- **Autofix** — 근본 원인 기반 코드 수정안 생성, 코딩 에이전트로 핸드오프
- **자연어 쿼리** — 트레이스·span 데이터를 자연어로 질의
- 비고 — 유료 플랜 애드온(활성 컨트리뷰터 과금)

## Session Replay · User Feedback

- **Session Replay** — 프론트엔드 세션 재생(백엔드에는 직접 해당 없음)
- **User Feedback** — 에러 발생 시 사용자 피드백 위젯/API 수집, 예외와 연결

## 데이터 거버넌스

- **PII 스크러빙** — 서버·클라이언트 측 민감정보 마스킹 규칙(`send-default-pii` 기본 off)
- **샘플링** — `traces-sample-rate` / `profiles-sample-rate` 로 물량·비용 제어
- **rate limiting·quota** — 프로젝트별 이벤트 상한, 스팸 이슈 억제

## 배포 형태

| 구분 | SaaS(sentry.io) | Self-hosted |
|------|-----------------|-------------|
| 초기 구축 | DSN 발급만 | `getsentry/self-hosted` docker-compose, ~20+ 컨테이너 |
| 요구 사양 | 없음 | CPU 4코어, RAM 16GB + swap 16GB(32GB 권장), 디스크 20GB |
| 운영 부담 | 없음 | 인프라·업그레이드 직접 |
| 데이터 소유 | 외부 저장 | 완전 통제 |
| 비용 | 무료 티어 + 사용량 | 인프라 리소스 |

- POC는 SaaS가 빠름, self-hosted는 데이터 통제·온프레미스 요건일 때

## Self-host 설치

- **직접 작성하는 Dockerfile은 없음** — Sentry는 Kafka·ClickHouse(Snuba)·Redis·Postgres·relay·worker 등 20+ 서비스로 구성돼, 공식 **`getsentry/self-hosted`** 저장소의 docker-compose + `install.sh`로 배포
- 사전 요구 — Docker 19.03.6+, Docker Compose 2.32.2+, 위 하드웨어 사양

```bash
VERSION=$(curl -Ls -o /dev/null -w %{url_effective} https://github.com/getsentry/self-hosted/releases/latest)
VERSION=${VERSION##*/}
git clone https://github.com/getsentry/self-hosted.git
cd self-hosted
git checkout ${VERSION}

./install.sh          # 이미지 pull·DB 마이그레이션·관리자 계정 생성 프롬프트

docker compose up --wait
```

- 접속 — `http://127.0.0.1:9000` (기본 포트 9000)
- 관리자 계정 — `install.sh` 실행 중 프롬프트로 생성(생략 시 최초 로그인에서 생성)
- 구성 파일 — `.env`, `sentry/config.yml`, `sentry/sentry.conf.py` 로 SMTP·도메인·기능 토글 설정
- 업그레이드 — 새 릴리스 태그로 checkout 후 `./install.sh` 재실행 (버전 순차 업그레이드 권장)
- 주의 — 다른 compose 스택과 포트(9000, Kafka·Postgres 등)·리소스 충돌 방지 위해 별도 호스트/네트워크 권장

### docker-compose.yml 구조 (참고용)

- **손으로 작성·유지하는 파일이 아님** — 실제 구동 compose는 `install.sh`가 `docker-compose.yml`을 기반으로 이미지 태그·볼륨을 채워 생성. 아래는 서비스 토폴로지 이해용 축약 스켈레톤(그대로 실행 대상 아님)
- 서비스 간 의존: `web/worker/cron → snuba → clickhouse`, 전 서비스 → `kafka·redis·postgres`

```yaml
x-sentry-defaults: &sentry
  image: getsentry/sentry:nightly
  depends_on: [postgres, redis, kafka, snuba-api, symbolicator]
  environment:
    SENTRY_CONF: /etc/sentry
  volumes:
    - sentry-data:/data

services:
  # --- 프론트/앱 계층 ---
  nginx:
    image: nginx:1.25-alpine
    ports: ["9000:80"]          # 접속 진입점
    depends_on: [web, relay]
  web:                           # Sentry 웹 UI/API
    <<: *sentry
    command: run web
  worker:                        # Celery 백그라운드 처리
    <<: *sentry
    command: run worker
  cron:                          # 스케줄러(beat)
    <<: *sentry
    command: run cron
  relay:                         # 이벤트 수집 인입 프록시
    image: getsentry/relay:nightly
    depends_on: [kafka, redis, web]

  # --- 이벤트 처리(검색/집계) ---
  snuba-api:                     # ClickHouse 질의 계층
    image: getsentry/snuba:nightly
    depends_on: [clickhouse, kafka, redis]
  symbolicator:                  # 스택트레이스 심볼화
    image: getsentry/symbolicator:nightly
  vroom:                         # 프로파일링 처리
    image: getsentry/vroom:nightly
    depends_on: [kafka, clickhouse]

  # --- 데이터 스토어 ---
  postgres:                      # 메타데이터(프로젝트·이슈·유저)
    image: postgres:14-alpine
    volumes: ["pg-data:/var/lib/postgresql/data"]
  redis:                         # 캐시·큐·버퍼
    image: redis:6.2-alpine
    volumes: ["redis-data:/data"]
  kafka:                         # 이벤트 스트림(KRaft)
    image: apache/kafka:3.8.0
    volumes: ["kafka-data:/var/lib/kafka/data"]
  clickhouse:                    # 이벤트 OLAP 저장(Snuba 백엔드)
    image: clickhouse/clickhouse-server:24.3
    volumes: ["clickhouse-data:/var/lib/clickhouse"]
  memcached:
    image: memcached:1.6-alpine

volumes:
  sentry-data:
  pg-data:
  redis-data:
  kafka-data:
  clickhouse-data:
```

- 실제 저장소 compose에는 위에 더해 `snuba-*consumer`, `post-process-forwarder`, `subscription-consumer`, `ingest-*`, `geoipupdate`, `smtp` 등 소비자·부가 서비스가 다수 추가됨
- 실 구동은 위 스켈레톤이 아니라 **`getsentry/self-hosted` 저장소 + `install.sh`** 사용

## Spring Boot 연동 (개요)

- 아티팩트 — Boot 3: `io.sentry:sentry-spring-boot-starter-jakarta` / Boot 4: `io.sentry:sentry-spring-boot-4` (SDK 8.x 계열)
- 최소 연동 — `application.yml`에 `sentry.dsn` + `sentry.environment` / `release`, 스타터가 미처리 예외·MVC 예외 자동 캡처
- 처리 예외·커스텀 컨텍스트는 `@RestControllerAdvice` + `Sentry.captureException`
- 자동 통합 — logback appender(로그→breadcrumb/이슈), WebMVC·JDBC 계측 연동
- OTel 연동 — `sentry-opentelemetry-otlp-*` 모듈로 기존 OTLP 트레이스를 Sentry에도 전송, trace ID 상관관계로 에러↔트레이스 연결

## 관측성에서의 위치

- Sentry 강점 = **에러 트래킹**(이슈 집계·그룹핑·알림·릴리스 헬스) — 로그/메트릭 수집기에 등가물 없음
- 트레이싱·프로파일링·로그도 제공하나 전용 도구(Tempo·Pyroscope·Loki 등)와 기능 중복
- **인프라/시스템 메트릭은 커버 못 함** — 커스텀 메트릭 베타 2024-10-07 종료, 현재 span 파생 메트릭뿐 → Prometheus류는 별도 필요
- 결론 — Sentry 단독으로 전체 관측성 대체는 불가, **에러 트래킹 축**으로 도입이 정석
