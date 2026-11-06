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
