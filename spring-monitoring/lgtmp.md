# LGTMP 구성 가이드

- 이 프로젝트가 구현한 **LGTMP 스택**과 **OpenTelemetry(OTel)·OTLP** 와의 관계 정리
- `LGTM` = Loki / Grafana / Tempo / Mimir, `+P` = Pyroscope
- 관측가능성 4기둥 — metrics · logs · traces · profiles

## 신호별 구현

| 신호 | 앱 라이브러리 | 프로토콜 | 엔드포인트 | 백엔드 |
|------|---------------|----------|------------|--------|
| metrics | micrometer-registry-prometheus | Prometheus text (pull) | `/actuator/prometheus` | Prometheus → Mimir |
| traces | micrometer-tracing-bridge-otel + opentelemetry-exporter-otlp | **OTLP/HTTP** | `:4318/v1/traces` | Tempo |
| logs | loki-logback-appender(loki4j) | Loki push API | `:3100/loki/api/v1/push` | Loki |
| profiles | pyroscope agent(async-profiler) | Pyroscope ingest | `:4040` | Pyroscope |

- **OTLP를 쓰는 신호는 traces 하나** — 나머지는 각 백엔드 네이티브 프로토콜 사용

## OTel / OTLP 개념

- **OpenTelemetry(OTel)** — 벤더 중립 관측 표준. API + SDK + 프로토콜(OTLP)로 traces·metrics·logs를 다룸 (profiles는 실험 단계)
- **OTLP(OpenTelemetry Protocol)** — 텔레메트리 전송 와이어 프로토콜
  - gRPC `:4317`, HTTP/protobuf `:4318`
  - Collector나 백엔드(Tempo 등)로 push
- **Micrometer** — Spring의 메트릭 + Observation 파사드
  - Observation API에 한 번 계측하면 metric·trace로 동시 발행
  - `micrometer-tracing-bridge-otel` 이 Observation → OTel SDK 로 연결, OTLP exporter가 전송

## 트레이스 경로 (OTLP 사용)

```
비즈니스 코드/자동계측
   └─ Micrometer Observation API
        └─ micrometer-tracing-bridge-otel  (OTel SDK span 생성)
             └─ opentelemetry-exporter-otlp (OTLP/HTTP)
                  └─ Tempo :4318  → Grafana
```

- WebMVC·JDBC 자동 계측이 `controller → service → jdbc` 스팬 생성 — 비즈니스 코드 비침투
- traceId/spanId는 MDC에 자동 주입 → Loki 로그와 상관관계 연결

## 메트릭이 OTLP가 아닌 이유

- Micrometer Prometheus registry로 `/actuator/prometheus` 노출 → Prometheus가 **pull(scrape)** → Mimir로 `remote_write`
- 커스텀 메트릭은 `@Counted` 선언형 애너테이션 사용 — Spring AOP가 `CountedAspect` 자동 구성, 비즈니스 코드 비침투
- OTLP push 방식(`micrometer-registry-otlp` 또는 `spring-boot-starter-opentelemetry`)을 쓰면 Prometheus 스크랩 경로가 사라짐
- "Prometheus 스크랩 → Mimir 저장" 이라는 LGTMP 표준 흐름 유지 목적

## 대안: 풀 OTel 구성

- `spring-boot-starter-opentelemetry` 로 metrics·traces·logs 전부 OTLP push 가능
- 앱 → OTel Collector(또는 Grafana Alloy) → Mimir/Tempo/Loki 팬아웃
- 장점 — 단일 프로토콜·단일 수집기로 통합
- 단점 — 수집기 컴포넌트 추가, Prometheus pull 모델 포기
- 이 프로젝트는 단순성과 pull 모델 유지를 위해 traces만 OTLP로 채택

## 상관관계(correlation)

- 공통 키 = `service.name`(OTLP resource, 기본값 `spring.application.name`) / 메트릭 `application` 태그 / Loki `app` 라벨
- Grafana 연동 — Tempo 트레이스 ↔ Loki 로그(traceId), Tempo ↔ Mimir(메트릭/service map)
