# DPoP Proof JWT

## DPoP Proof JWT (Demonstrating Proof of Possession, 소유 증명)

- RFC 9449 (OAuth 2.0 Demonstrating Proof of Possession)
- 클라이언트가 특정 공개키에 대응하는 개인키를 보유하고 있음을 매 http 요청마다 증명하는 용도로 사용
- 일반적인 Bearer 토큰은 토큰을 가진 누구나 사용 가능
    - 서버는 Bearer 토큰이 유효한지만 확인하고 누가 보냈는지 출처에 대한 확인은 없음
    - 때문에 토큰이 탈취 당했을 때 공격자는 그대로 사용 가능
- DPoP Proof JWT 는 accessToken 을 발행받은 클라이언트가 아닌 다른 클라이언트가 accessToken 을 탈취하여 사용하는 것을 방지하기 위함

## 브라우저 -> api 서버 bearer jwt 인증 flow

1. 브라우저 -> api 서버 로그인 시 jwt 발급
2. 이후 api 요청시 발급받은 jwt 를 포함
3. 서버는 요청의 jwt 의 유효성 검증

```
서버는 jwt 의 유효성만을 검증
jwt 가 탈취되어 다른 어떤 주체가 사용해도 알 수 없음
```

## 브라우저 -> api 서버 DPoP jwt 인증 flow

### 클라이언트 측에서 개인키/공개키 쌍 생성

- 키쌍은 재사용
- 개인키는 클라이언트 밖으로 나가지 않음

### 로그인 api 호출

- 개인키/공개키 쌍으로 DPoP Proof(JWT) 생성
- 생성한 DPoP Proof 는 `DPoP` 헤더에 포함
- 서버가 발급한 nonce 를 `nonce` 클레임에 포함 ([서버 nonce](#서버-nonce) 참고)

#### DPoP Proof JWT 예시

- 페이로드 영역은 매 요청마다 바뀜
- typ: `dpop+jwt` 고정
- jwk: 클라이언트 공개키 (개인키 포함 시 거절)
- jti (JwtId): 중복되지 않도록 클라이언트에서 책임
- htm/htu (http method/http uri): 대상 api 에 대한 http 요청 정보
- iat (Issued At): Proof 생성시간
- nonce: 서버가 `DPoP-Nonce` 헤더로 내려준 값
- ath (Access Token Hash): proof 가 어떤 AT와 함께 쓰이는지 묶어주는 클레임
    - ath = base64url(sha-256(AT))
    - 로그인 시점엔 AT 가 없으므로 로그인 proof 에는 없음, AT 를 사용하는 api 호출 proof 에만 포함

```
{
  "typ": "dpop+jwt",
  "alg": "ES256",
  "jwk": {
    "kty": "EC",
    "crv": "P-256",
    "x": "gE_Wj5aOKoNwQ07V_731_528M2wtt4nqBrdrrCDP82Q",
    "y": "ikZJHUqwuHfeXaRQpgsnc0QDRnoLMNOqjXGmEV76NVU"
  }
}

// 로그인 proof
{
  "jti": "c72ec957-...",
  "htm": "POST",
  "htu": "http://localhost:8080/login",
  "iat": 1793945806,
  "nonce": "AAAAAAHIOVjWHNG7zlajCDjU-exKrcKddvEGxK1Z8WuPIKGQ-Z0dEg"
}

// api 호출 proof
{
  "jti": "ceae5b71-a448-4144-963b-4bb365767c1a",
  "htm": "GET",
  "htu": "http://localhost:8080/api/items",
  "iat": 1793945830,
  "nonce": "AAAAAAHIOVjWHNG7zlajCDjU-exKrcKddvEGxK1Z8WuPIKGQ-Z0dEg",
  "ath": "hHkaQeixNWiybC6ICtf7mSydDKYMg4m3eOaOdwswrc4"
}
```

### 서버측 로그인 처리 (AT 발급)

- `LoginService` -> `DPoPJwtTokenIssuer`
- `DPoP` 헤더는 정확히 1개만 허용 (RFC 9449 4.3)
- dpop proof 검증 (`DPoPProofJwtDecoderFactory`)
    - `typ` = `dpop+jwt`, alg 는 RSA/EC 계열, jwk 는 공개키인지 검증
    - proof 헤더의 공개키(jwk)로 서명 검증
    - 호출된 api가 htm/htu 와 일치하는지 검증
    - iat 가 현재 시각 ±30초 이내인지 검증
    - jti 중복여부 검증 (iat 기준 30초 동안 동일 jti 재사용 차단)
    - nonce 검증 (`DPoPNonceValidator`)
    - 실패 시 400 `invalid_dpop_proof`, nonce 누락/만료 시 400 `use_dpop_nonce`
- username/password 검증
    - 실패 시 400 `invalid_grant` (RFC 6749 5.2)
- AT 발급 (ES256, 유효기간 5분)
- AT 의 `cnf.jkt` 클레임에는 proof 공개키의 지문(RFC 7638 SHA-256 thumbprint) 포함

```
// 로그인 응답 (RFC 6749 5.1 형식, Cache-Control: no-store)
{"access_token": "eyJraWQiOiI1YTJm...", "token_type": "DPoP", "expires_in": 300}

// 오류 응답 (RFC 6749 5.2 형식)
{"error": "use_dpop_nonce", "error_description": "Authorization server requires nonce in DPoP proof."}

// AccessToken 디코딩 결과
{"kid": "5a2f2349-...", "alg": "ES256"}

{
  "sub": "user",
  "iat": 1793945806,
  "exp": 1793946106,
  "cnf": {
    "jkt": "_6605d9fKsp3mY7sJJl4akXivDf-jvqOkh8ie858lTA"
  }
}
```

- 학습용 구현으로 `iss`, `aud`, `jti`, `typ: at+jwt` 미포함
- 서명키는 기동 시 메모리에 생성 (재시작 시 기존 AT 무효)

### 인증 필요 API 호출

- 클라이언트 측에서 dpop proof 새로 생성
    - dpop proof 는 매 번 새로 생성되어야 함
- Authorization 헤더에는 AT를, DPoP 헤더에는 proof 를 포함하여 요청
- `Authorization: Bearer <AT>` 로 보내면 거절 (DPoP 바인딩 토큰의 Bearer 다운그레이드 차단)

```
GET /api/items HTTP/1.1
Host: localhost:8080
Authorization: DPoP eyJraWQiOiI1YTJm...
DPoP: eyJ0eXAiOiJkcG9wK2p3dCIs...
```

### 인증 필요 api 서버측 검증

- Spring Security `oauth2ResourceServer { dPoP { } }` + `DPoPAuthenticationProvider` 사용
- Authorization 헤더로부터 AT 파싱
- AT 검증 (`JwtDecoder`)
    - 서명, exp 검증
    - 이 프로젝트는 iss, aud 검증 미설정
- proof 검증 (`DPoPProofJwtDecoderFactory`)
    - 서명 검증 (proof 헤더 jwk 로 proof 서명 검증)
    - htm/htu 검증
    - iat 검증 (±30초)
    - jkt 검증
        - 이 proof를 서명한 키가 AT 주인의 키가 맞는지 검증
        - proof 의 공개키 지문과 AT 의 cnf.jkt 비교
        - AT를 탈취한 공격자가 자신의 키로 proof 를 만드는 것 방지
    - ath 검증
        - 이 proof 가 현재 요청에 포함된 AT 를 위해 생성된 것이 맞는지 검증
        - Authorization 헤더의 `DPoP ` 뒤 AT 를 base64url(sha-256(AT)) 해서 ath 와 비교
    - jti 검증
        - iat 로부터 30초 동안 동일 jti 가 요청된 경우 해당 요청은 재전송된 proof 로 취급
    - nonce 검증 (`DPoPNonceValidator`)
- 실패 시 401 + `WWW-Authenticate: DPoP error="invalid_dpop_proof"`, nonce 문제 시 `error="use_dpop_nonce"`

```
DPoP 방식 적용시 AccessToken 탈취된 경우 클라이언트의 개인키만 안전하다면 공격자가 유효한 DPoP Proof 를 만들어 낼 수 없으므로 
탈취된 액세스 토큰 재사용 불가

단, AccessToken 을 만들어내는 서버측 서명키(jwt secret)이 털린 경우 공격자는 accessToken, DPoP Proof 모두 맘대로 만들어낼 수 있음
즉, DPoP Proof JWT 적용시에도 jwt 서명키 회전 또는 서명키 자체를 안전하게 관리하는(HSM, KMS...) 것 병행 필요
```

## Claim

- htm, htu (http method/http uri)
    - proof로 보낼 목적지의 url 및 http 메서드 명시
    - htm, htu 가 없을 때, proof 로 확인 가능한 것은 이 proof 의 개인키를 가진 클라이언트가 서명했다는 것은 확신 가능하지만 클라이언트가 목적으로 한 api 호출인지는 확신 불가
    - proof 구성시 클라이언트는 목적 api 를 htm, htu 에 명시
    - 서버는 클라이언트가 목적지로 삼은 htm, htu 와 현재 호출된 api 가 동일한지 검증
    - 즉, 클라이언트가 proof 구성시 목적지로 지정한 api가 호출되었는지 서버에서 한 번 더 검증 가능
    - `유출된 proof의 다른 API 재사용 차단 목적`
- ath (AccessTokenHash)
    - proof 가 어떤 AT와 함께 쓰이는지 묶어주는 클레임
    - AT를 발행받은 클라이언트가 proof 생성시 proof 의 ath 클레임에 AT 해시를 포함시켜서 proof 와 AT를 묶는 용도
- jti, iat
    - 재전송 방지용 ([DPoP proof 재전송 방지](#dpop-proof-재전송-방지) 참고)
- nonce
    - 서버가 지정한 최근 시간대에 만들어진 proof 만 허용하는 용도 ([서버 nonce](#서버-nonce) 참고)

## DPoP proof 재전송 방지

- `iat`(Issued At): proof 생성 시각
- `jti`(JWT ID): proof 고유 식별자
- `iat`로 허용 시간 범위 제한, 그 범위 안의 동일 `jti` 재사용은 차단
- 허용 오차(30초)가 지난 proof는 `iat` 검사에서 거절되므로 `jti`는 30초만 보관
- jti 고유성 생성은 클라이언트 책임, 중복 여부 판단은 서버 캐시 책임

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

## 서버 nonce

- 서버가 `DPoP-Nonce` 응답 헤더로 nonce 발급
    - `DPoPNonceFilter` 통해 매 응답마다 `DPoP-Nonce` 헤더에 nonce 포함
- 클라이언트는 nonce를 proof `nonce` claim에 넣어 서명
- `jti`는 같은 proof 재사용 차단, nonce는 미리 만든 proof 차단
- 공격자가 `iat` 를 미래시점으로 하여 `jti` 까지 달리하여 미리 생성 가능했다면 이를 막을 방법없음
- 서버 nonce 를 통해 서버가 지정한 최근 시간동안 만들어진 proof 만을 허용
- 로그인과 리소스 api 모두 nonce 필수
- 서명/htm/htu 등 다른 검증 실패가 있으면 nonce 오류가 아닌 `invalid_dpop_proof` 로 응답

```
1. POST /login (proof에 nonce 없음)
- 400 {"error":"use_dpop_nonce"} 및 DPoP-Nonce 헤더 응답

2. POST /login (proof 에 nonce 포함)
- 200 ok
- access token 및 DPoP-Nonce 헤더 응답

3. GET /api/items (proof 에 nonce 없음 또는 만료)
- 401, WWW-Authenticate: DPoP error="use_dpop_nonce" 및 DPoP-Nonce 헤더 응답
- 클라이언트는 새 nonce 로 proof 재생성 후 재요청
```

### Stateless HMAC

- nonce = `카운터 + HMAC(secret, 카운터)`, 카운터는 `현재 시각 / 1분`
- 카운터로 발급 시점, HMAC으로 서버 발급 여부 확인 가능하여 별도 nonce 검증을 위한 서버측 저장소 불필요
- 카운터가 현재·직전 값이고 HMAC 재계산 값과 일치하면 통과 (유효기간 1~2분)
- secret 없이 HMAC 계산 불가로 위조·미래 nonce 생성 불가
- secret 은 기동 시 랜덤 생성이므로 인스턴스 간 secret 공유 필요

## 구현 클래스

- `SecurityConfig`: resource server + DPoP 설정, proof 검증 팩토리에 nonce 검증 추가
- `DPoPJwtTokenIssuer`: 로그인 proof 검증 및 `cnf.jkt` 포함 AT 발급
- `DPoPNonceProvider`: HMAC 기반 nonce 발급/검증
- `DPoPNonceFilter`: 모든 응답에 `DPoP-Nonce` 헤더 추가
- `DPoPNonceValidator`: proof `nonce` 클레임 검증
- `DPoPNonceAuthenticationEntryPoint`: 리소스 api nonce 오류 시 `use_dpop_nonce` 응답
- `DPoPTest`: 로그인/CRUD, 다른 키·htm/htu/ath 불일치, 만료·재전송 proof, Bearer 다운그레이드, nonce 시나리오 테스트

## Reference

- https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/dpop-tokens.html
- https://www.ibm.com/docs/ko/security-verify?topic=connect-demonstrating-proof-possession
- https://www.rfc-editor.org/rfc/rfc9449.html#section-4.2
