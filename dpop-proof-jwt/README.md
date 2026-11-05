# dpop-proof-jwt

## DPoP proof 재전송 방지

- `iat`(Issued At): proof 생성 시각
- `jti`(JWT ID): proof 고유 식별자
- `iat`로 허용 시간 범위 제한, 그 범위 안의 동일 `jti` 재사용은 차단
- 허용 오차(30초)가 지난 proof는 `iat` 검사에서 거절되므로 `jti`는 30초만 보관

### 기본 동작 (Spring Security 7.1.2)

| 항목 | 기본값 |
|---|---|
| `iat` 허용 오차 | ±30초 |
| `jti` 기록 유지 기간 | 30초 |
| 저장소 | `DPoPProofReplayValidator.InMemoryCache` |
| 캐시 최대 크기 | 100,000개 |
| 공개키 지문(jkt)당 최대 기록 수 | 1,000개 |
| 만료 항목 정리 주기 | 10초 |


### 운영 환경

- 메모리 캐시는 인스턴스 간 공유 불가로 다른 인스턴스로의 재전송 차단 불가
- 재시작 시 기록 유실
- Redis 등 공유 저장소 사용 필요
- `DPoPProofReplayValidator(cache)` 생성 후 `DPoPProofJwtDecoderFactory.createDefaultJwtValidatorFactory(listOf(...))`로 교체
- 리소스 서버 쪽은 `DPoPAuthenticationProvider.setDPoPProofVerifierFactory(...)`로 같은 팩토리 주입

## Reference
- https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/dpop-tokens.html
