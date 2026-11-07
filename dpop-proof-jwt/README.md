# DPoP Proof JWT

## DPoP Proof JWT (Demonstrating Proof of Possession, 소유 증명)

- RFC 9449 (OAuth 2.0 Demonstrating Proof of Possession)
- 클라이언트가 특정 공개키에 대응하는 개인키를 보유하고 있음을 매 http 요청마다 증명하는 용도로 사용
- 일반적인 Bearer 토큰은 토큰을 가진 누구나 사용 가능
  - 서버는 Bearer 토큰 이 유효한지만 확인하고 누가 보냈는지 출처에 대한 확인은 없음
  - 때문에 토큰이 탈취 당했을 때 공격자는 그대로 사용 가능
- DPoP Proof JWT 은 accessToken 을 발행받은 클라이언트가 아닌 다른 클라이언트가 accessToken 을 탈취하여 사용하는 것을 방지하기 위함

## 브라우저 -> api 서버 bearer jwt 인증 flow

1. 브라우저 -> api 서버 로그인 시 jwt 발급
2. 이후 api 요청시 발급받은 jwt 를 포함
3. 서버는 요청의 jwt 의 유효성 검증

```
서버는 jwt 의 유효성만을 검증
jwt 가 탈취되어 다른 어떤 주체가 사용해도 알 수 없음
```

## 브라우저 -> api 서버 DPoP jwt 인증 flow

1. 브라우저가 키 쌍(개인키/공개키) 생성
2. 브라우저 -> api 서버 로그인 시 DPoP Proof(JWT, 정확히는 JWS) 전송
- DPoP Proof(JWS) 파싱시 클라이언트의 공개키를 알 수 있음
- 공개키로 DPoP Proof 서명 검증
- 검증 성공시, 서버는 jwt 발급시 이 공개키의 주인만 사용 가능 하다는 것을 기록 (`cnf.jkt` 클레임 추가)
- `cnf.jkt` 에 공개키의 해시값 (지문) 을 포함

서버가 응답하는 jwt 예시

```
{
  "access_token": "eyJhbGciOiJFUzI1NiIsImtpZCI6IkJlQUxrYiJ9.eyJzdWIiOiJzb21lb25lQGV4YW1wbGUuY29tIiwiaXNzIjoi...",
  "token_type": "DPoP",
  "expires_in": 3600
}
```

access token 파싱시 예시

```
{
  "sub": "someone@example.com",
  "iss": "<https://server.example.com>",
  "exp": 1562266216,
  "cnf": {
    "jkt": "0ZcOCORZNYy-DWpqq30jZyJGHTN0d2HglBV3uiguA4I"
  }
}
```

1. 이후 api 요청시 jwt와 개인키로 새롭게 만든 DPoP Proof(JWS)를 요청에 포함
- 매 요청마다 클라이언트는 본인의 개인키로 DPoP Proof 를 새롭게 만들어서 요청에 포함
- 클라이언트는 매 번 새롭게 생성. 만약 재사용한다면 bearer 와 다를게 없음
- DPoP Proof 클레임 중 `jti` 가 nonce와 유사한 역할을 해서 서버는 `jti` 를 기반으로 재전송이 있는지 검증 필요

```
// 요청 http header 예시
Authorization: DPoP <액세스 토큰>
DPoP: <새 Proof>
```

1. 서버측 검증
- 기본적인 jwt 유효성 검증
- 이 `DPoP: <새 Proof>` 가 공개키의 주인이 만든 것이 맞는지 검증
  - `verify(jwk, "Proof의 헤더.페이로드", Proof의 서명)`
- 공개키가 로그인 때 등록된 공개키와 같은지 검증
  - accessToken 의 `cnf.jkt` 와 새 Proof 의 공개키 지문 동일 여부 확인
  - Proof 의 공개키가 엑세스 토큰 발급시 사용된 공개키라는 것을 확인

```
DPoP 방식 적용시 AccessToken 탈취된 경우 클라이언트의 개인키만 안전하다면 공격자가 유효한 DPoP Proof 를 만들어 낼 수 없으므로 
탈취된 액세스 토큰 재사용 불가

단, AccessToken 을 만들어내는 서버측 서명키(jwt secret)이 털린 경우 공격자는 accessToken, DPoP Proof 모두 맘대로 만들어낼 수 있음
즉, DPoP Proof JWT 적용시에도 jwt 서명키 회전 또는 서명키 자체를 안전하게 관리하는(HSM, KMS...) 것 병행 필요
```

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

## 서버 nonce

- 서버가 `DPoP-Nonce` 응답 헤더로 nonce 발급
  - `DPoPNonceFilter` 통해 매 요청마다 `DPoP-Nonce` 헤더에 nonce 응답  
- 클라이언트는 nonce를 proof `nonce` claim에 넣어 서명
- `jti`는 같은 proof 재사용 차단, nonce는 미리 만든 proof 차단
- 공격자가 `iat` 를 미래시점으로 하여 `jti` 까지 달리하여 미리 생성 가능했다면 이를 막을 방법없음
- 서버 nonce 를 통해 서버가 지정한 최근 시간동안 만들어진 proof 만을 허용

```
login api 호출시에도 nonce 사용됨

1. POST /login (proof에 nonce 없음)
- 400 {"error":"use_dpop_nonce"} 예외응답 및 nonce 헤더(DPoP-Nonce) 응답

2. POST/ login (proof 에 nonce 포함)
- 200 ok
- access token 및 nonce 헤더 응답
- 이후 api 호출시 accessToken 개인키 서명에 nonce 포함하여 요청
```

```
1. 로그인 (nonce 없음) → 400                                                                                                                                                                                                                                                                                        

// request                                                                                                                                                                                                                                                                                                                    
POST /login                                                                                                                                                                                                                                                                                                         
Content-Type: application/json                                                                                                                                                                                                                                                                                      
DPoP: eyJ0eXAiOiJkcG9wK2p3dCIs...                                                                                                                                                                                                                                                                                   

// token 디코딩 결과 예시
{"username":"user","password":"password"}
{"typ": "dpop+jwt", "alg": "ES256","jwk": { "kty": "EC", "crv": "P-256", "x": "G5ZhyOf6...", "y": "czRMP2HL..." } }                                                                                                                                                                                                                                                                                                                    
{"jti": "a3a65806-...", "htm": "POST", "htu": "http://localhost:8080/login", "iat": 1793945805 }

// response
HTTP/1.1 400                                                                                                                                                                                                                                                                                                        
DPoP-Nonce: AAAAAAHIOVjWHNG7zlajCDjU-exKrcKddvEGxK1Z8WuPIKGQ-Z0dEg

2. 로그인 (nonce 포함) → 200                                                                                                                                                                                                                                                                                        

// request                                                                                                                                                                                                                                                                                                          
POST /login                                                                                                                                                                                                                                                                                                         
Content-Type: application/json                                                                                                                                                                                                                                                                                      
DPoP: eyJ0eXAiOiJkcG9wK2p3dCIs...                                                                                                                                                                                                                                                                                   

{"username":"user","password":"password"}                                                                                                                                                                                                                                                                           

// DPoP 디코딩 결과                                                                                                                                                                                                                                                                                                 
{"typ": "dpop+jwt", "alg": "ES256", "jwk": {"kty": "EC", "crv": "P-256", "x": "G5ZhyOf6...", "y": "czRMP2HL..."}}                                                                                                                                                                                                   
{"jti": "c72ec957-...", "htm": "POST", "htu": "http://localhost:8080/login", "iat": 1793945806, "nonce": "AAAAAAHIOVjWHNG7zlajCDjU-exKrcKddvEGxK1Z8WuPIKGQ-Z0dEg"}                                                                                                                                                  

// response                                                                                                                                                                                                                                                                                                         
HTTP/1.1 200                                                                                                                                                                                                                                                                                                        
DPoP-Nonce: AAAAAAHIOVjWHNG7zlajCDjU-exKrcKddvEGxK1Z8WuPIKGQ-Z0dEg                                                                                                                                                                                                                                                  

{"accessToken": "eyJraWQiOiI1YTJm...", "tokenType": "DPoP", "expiresIn": 300}                                                                                                                                                                                                                                       

// accessToken 디코딩 결과                                                                                                                                                                                                                                                                                          
{"kid": "5a2f2349-...", "alg": "ES256"}                                                                                                                                                                                                                                                                             
{"sub": "user", "cnf": {"jkt": "_6605d9fKsp3mY7sJJl4akXivDf-jvqOkh8ie858lTA"}, "iat": 1793945806, "exp": 1793946106}
```

### Stateless HMAC

- nonce = `카운터 + HMAC(secret, 카운터)`, 카운터는 `현재 시각 / 1분`
- 카운터로 발급 시점, HMAC으로 서버 발급 여부 확인 가능하여 별도 nonce 검증을 위한 서버측 저장소 불필요
- 카운터가 현재·직전 값이고 HMAC 재계산 값과 일치하면 통과
- secret 없이 HMAC 계산 불가로 위조·미래 nonce 생성 불가
- 인스턴스 간 secret 공유 필요

## Reference
- https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/dpop-tokens.html
- https://www.ibm.com/docs/ko/security-verify?topic=connect-demonstrating-proof-possession
- https://www.rfc-editor.org/rfc/rfc9449.html#section-4.2
