# spring-oidc

- OIDC Provider(인가서버) + Resource Server 를 **단일 애플리케이션**에서 제공하는 샘플

## FilterChain 구성

- `@Order(1)` 인가서버 체인 — `securityMatcher(configurer.endpointsMatcher)` 로 OAuth2/OIDC 엔드포인트만 매칭
  - `oidc(withDefaults())` 로 `/.well-known/openid-configuration`, `/userinfo`, `/connect/logout` 활성
  - HTML 요청은 `/login` 으로 리다이렉트(`LoginUrlAuthenticationEntryPoint` + `MediaTypeRequestMatcher`)
  - `/userinfo` 가 access token 을 받도록 같은 체인에 `oauth2ResourceServer(jwt)` 추가
- `@Order(2)` 리소스서버 체인 — `/api/**`, stateless, JWT 검증(`SCOPE_read` 권한 체크)
- `@Order(3)` 기본 체인 — formLogin/logout, H2 콘솔

- 인가서버가 서명한 JWT 를 같은 앱이 검증 → `JwtDecoder` 는 `OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)` 로 동일 `JWKSource` 공유
- Spring Security 7 부터 `OAuth2AuthorizationServerConfigurer.authorizationServer()` 정적 팩터리 대신 생성자 + `http.with(configurer)` 또는 `http.oauth2AuthorizationServer { }` 사용

## 클라이언트 / 계정

| clientId | secret | grant | scope |
|---|---|---|---|
| oidc-client | oidc-secret | authorization_code, refresh_token | openid, profile, email, read |
| service-client | service-secret | client_credentials | read |

- redirect_uri: `http://127.0.0.1:9000/authorized`
- 계정: `user` / `password` (ROLE_USER)
- consent 화면 사용(`requireAuthorizationConsent(true)`), 테스트 편의를 위해 PKCE 미강제

## 토큰 커스터마이징

- `OAuth2TokenCustomizer<JwtEncodingContext>` 하나로 분기
  - id_token → `email`, `nickname` 추가 → `/userinfo` 응답에 그대로 노출(기본 매퍼가 id_token claim 사용)
  - access_token → `roles` 추가
  - client_credentials 는 사용자 없으므로 skip

## 실행 / 확인

```bash
./gradlew bootRun          # http://localhost:9000
```

```bash
# 1. client_credentials
curl -s -u service-client:service-secret \
  -d grant_type=client_credentials -d scope=read \
  http://localhost:9000/oauth2/token

# 2. authorization_code: 브라우저 접속 후 로그인 + 동의 → /authorized 에서 code 확인
open "http://localhost:9000/oauth2/authorize?response_type=code&client_id=oidc-client&scope=openid%20profile%20email%20read&redirect_uri=http://127.0.0.1:9000/authorized&state=xyz"

# 3. code 교환
curl -s -u oidc-client:oidc-secret \
  -d grant_type=authorization_code -d code=$CODE \
  -d redirect_uri=http://127.0.0.1:9000/authorized \
  http://localhost:9000/oauth2/token

# 4. OIDC userinfo / 리소스서버
curl -s -H "Authorization: Bearer $AT" http://localhost:9000/userinfo
curl -s -H "Authorization: Bearer $AT" http://localhost:9000/api/me
curl -s -H "Authorization: Bearer $AT" http://localhost:9000/api/scoped/resource
```

- 응답 예시

```json
// /userinfo
{"sub":"user","nickname":"테스트유저","email":"user@study.com"}
// /api/me
{"subject":"user","issuer":"http://localhost:9000","scopes":["read","openid","profile","email"],"roles":["USER"]}
```

- `test/oidc.http` 로도 전체 플로우 확인 가능

## 운영 적용 시 교체 지점

- `jwkSource()` — 매 기동 시 RSA 키 생성 → JWKS 파일/KMS/Secret Manager 로 고정 필요
- `InMemoryRegisteredClientRepository` → `JdbcRegisteredClientRepository`
- `InMemoryOAuth2AuthorizationService` → `JdbcOAuth2AuthorizationService` (스키마: `oauth2-authorization-schema.sql`)
