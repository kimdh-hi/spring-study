# Proto Contracts

이 모듈은 gRPC/protobuf 계약의 단일 원본입니다.

`order-service`, `product-service` 같은 서비스는 `.proto` 파일을 직접 복사해서 들고 있지 않고, 이 모듈의 계약을 의존성으로 가져와 stub을 생성합니다.

## 디렉터리 규칙

- `.proto` 파일은 `src/main/proto` 아래에 둡니다.
- 디렉터리 경로는 protobuf `package`와 맞춥니다.
- protobuf `package`의 마지막 요소는 `v1`, `v2` 같은 major API version으로 끝냅니다.
- generated Java/Kotlin package는 직접 작성한 서비스 코드 package와 분리합니다.

예시:

```text
src/main/proto/com/example/product/v1/product_service.proto
package com.example.product.v1;
```

## 호환성 규칙

- 한 번 사용한 field number는 재사용하지 않습니다.
- 삭제한 field number와 field name은 `reserved`로 남깁니다.
- 기존 `v1`에는 backward-compatible 변경만 추가합니다.
- breaking change가 필요하면 기존 `v1`을 수정하지 않고 `com.example.product.v2` 같은 새 package를 만듭니다.

## 메시지 작성 규칙

- RPC마다 request/response message를 따로 둡니다.
- request/response message 이름은 RPC 이름을 기준으로 짓습니다.
- 예: `GetProduct` RPC는 `GetProductRequest`, `GetProductResponse`를 사용합니다.
- enum의 zero value는 `*_UNSPECIFIED = 0` 형태로 둡니다.

## 검증

Buf CLI를 설치한 뒤 실행합니다.

```shell
./gradlew protoCheck
```

breaking change 검사는 비교할 이전 schema source를 지정해야 합니다.

```shell
./gradlew bufBreaking -PbufBreakingAgainst='.git#branch=main,subdir=proto-contracts'
```

## 참고 자료

- Protobuf Best Practices: https://protobuf.dev/best-practices/dos-donts/
- Protobuf proto3 업데이트 규칙: https://protobuf.dev/programming-guides/proto3/#updating
- Google API Versioning AIP-185: https://google.aip.dev/185
- Buf Style Guide: https://buf.build/docs/best-practices/style-guide/
- Buf Breaking Change Detection: https://buf.build/docs/breaking/
