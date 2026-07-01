# spring-monitoring

- Spring Boot 4 + Kotlin 앱을 **Grafana LGTM+P 스택**으로 모니터링하는 최소 샘플
- `LGTM` = Loki / Grafana / Tempo / Mimir, `+P` = Pyroscope(profiling)
- 관측가능성 4기둥(metrics · logs · traces · profiles) 전부 다룸

## 구성 요소

- **L** Loki — 로그, loki4j 직접 push (`:3100`)
- **G** Grafana — 대시보드 / 상호연동 (`:3000`)
- **T** Tempo — 트레이스, OTLP/HTTP push (`:4318`)
- **M** Mimir — 메트릭 저장, Prometheus `remote_write` (`:9009`)
- **P** Pyroscope — 프로파일, pyroscope agent push (`:4040`)
- Prometheus — 앱 `/actuator/prometheus` 스크랩 후 Mimir로 공급하는 보조 스크래퍼 (`:9090`)

## 데이터 흐름

- 메트릭 — 앱 노출 → Prometheus pull → Mimir 저장 → Grafana 조회
- 트레이스 — 앱 → Tempo OTLP push → Grafana
- 로그 — 앱 → Loki push(traceId/spanId 포함) → Grafana
- 프로파일 — 앱 agent → Pyroscope push → Grafana
- 앱은 로컬 `./gradlew bootRun`, 백엔드 7종은 docker compose

## 기술 스택

- Spring Boot 4.1.0 / Kotlin 2.3.21 / Java 25 / Gradle 9.2.1
- MySQL 8.4 + Spring Data JPA
- Micrometer(Prometheus registry) + Micrometer Tracing(OTLP bridge) + loki4j + Pyroscope agent

## 클린 아키텍처

```
com.study.monitoring
├── config      PyroscopeConfig    프로파일링 에이전트 기동
└── order
    ├── ui          OrderController REST, 엔티티 대신 DTO 노출
    ├── application OrderService    @Transactional, @Counted 커스텀 메트릭 + 로그
    └── domain      Order, repository/OrderRepository
```

## 실행

- 백엔드 기동 — `docker compose up -d`
- 앱 실행 — `./gradlew bootRun`
- 트래픽 — 아래 호출

```bash
curl -X POST localhost:8080/api/orders -H 'Content-Type: application/json' -d '{"product":"book","amount":3}'
curl localhost:8080/api/orders
```

## 확인 포인트

- 대시보드 — Grafana > Dashboards > "Spring Monitoring (LGTMP)" 자동 프로비저닝 (주문/HTTP/JVM/로그 패널)
- 메트릭 — Grafana Mimir에서 커스텀 `orders_created_total`·자동 `http_server_requests_seconds_count` 쿼리 (Prometheus `:9090` target `UP` 확인)
- 트레이스 — Grafana Tempo에서 `controller → service → jdbc` 스팬
- 로그 — Grafana Loki에서 `{app="spring-monitoring"}`, 동일 traceId 포함
- 프로파일 — Grafana Pyroscope에서 `spring-monitoring` flame graph (`:4040` UI 가능)
- 연동 — Tempo↔Loki traceId 클릭 이동

## 정리

- `docker compose down -v`

## Spring Boot 4 주의점

- 트레이스 OTLP 키 변경 — `management.otlp.tracing.endpoint` → `management.opentelemetry.tracing.export.otlp.endpoint`
- 엔드포인트 노출 — `management.endpoint.<id>.enabled` → `.access`
- 메트릭은 `spring-boot-starter-opentelemetry`(OTLP push) 대신 `micrometer-registry-prometheus`(scrape) 사용 — Prometheus 스크랩 경로 유지
