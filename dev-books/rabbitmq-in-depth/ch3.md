## ch3
메시지 속성 심층 탐사
- 메세지 속성, 속성이 메시지 전송에 미치는 영향

### 메시지 속성 적절히 사용하기

메시지는 세 가지 프레임으로 구성된다.
- Basic.Publish 메서드 프레임
- 콘텐츠 헤더 프레임
- 바디 프레임

콘텐츠 헤더 프레임은 `Basic.Properties` 를 포함한다.
`Basic.Properties` 에는 `delivery-mode` 와 같이 AMQP 스펙에 사전에 정의된 속성도 있지만 `type`과 같이 정확한 스펙이 없는 속성도 존재한다.

`delivery-mode`
메시지가 큐에 있을 때 메시지를 메모리에 보관할지, 디스크에 먼저 저장할지에 대한 설정이다.

```
메시지을 사용해서 메시지를 설명하는 것은 유용하지만 소비자 애플리케이션에서 메시지의 속성을 사용할 수 있는지 확인해야 한다. RabbitMQ 에서 MQTT 프로토콜을 사용하는 경우 AMQP 스펙에 정의된 특정 속성은 사용할 수 없게 된다.
```

#### AMQP 메시지 속성
- `content-type`: 메시지 본문 해석 방법
- `content-encoding`
- `message-id`, `correlation-id`: 메시지의 고유 식별값
- `timestamp`: 메시지의 크기를 줄인다??, 메시지 생성 시점의 표준시간
- `expiration`: 메시지 만료
- `delivery-mode`: 큐에 메시지 추가시 디스크에 저장할지, 메모리에 저장할지
- `app-id`, `user-id`: 발행자 애플리케이션 식별
- `type`: 발행자와 소비자 사이 계약을 정의
- `reply-to`: 패턴 값으로 응답 메시지를 라우팅
- `headers`: RabbitMQ 에 메시지 라우팅 시 커스텀 한 속성 전달

#### gzip, content-encoding 으로 메시지 크기 줄이기
AMQP 를 이용해서 전달되는 메시지는 기본적으로는 압축되지 않는다.
그렇기 때문에 너무 큰 메시지가 전달되거나 XML과 같은 지나치게 자세한 포맷으로 메시지가 전달되는 경우 문제가 될 수 있다.

서버에서 웹페이지를 `gzip` 으로 압축하고 브라우저에서 랜드링 전 압축을 푸는 것과 비슷하게 발행하는 메시지를 발행하기 전 메시지를 압축하고 소비자가 압축을 풀도록 할 수 있다.
위와 같은 처리를 `content-encoding` 속성으로 처리한다.
메시지 본문이 `base64` or `gzip` ... 과 같은 형식으로 인코딩 됐는지 알 수 있다.

```
content-encoding <-> content-type

HTTP 스펙과 마찬가지로 AMQP 에서도 content-encoding 은 content-type 을 넘어선 인코딩 수준을 나타낸다.
본문이 gzip 또는 다른 형식으로 압축됐음을 나타낸다.

일부 AMQP 클라이언트에서 content-encoding 을 UTF-8 로 기본설정하는데 이는 잘못된 것이다.
AMQP 스펙은 content-encoding 이 MIME 콘텐츠 인코딩을 저장하기 위함이라고 명시돼 있다.
```

SMTP의 경우 주로 Base64 형태로 본문을 인코딩 한다. 
이미지가 포함된 HTML 을 만드는 경우 이미지를 포함한 모든 본문을 Base64로 인코딩하게 된다.
이 때 인코딩 된 데이터는 SMTP 프로토콜의 범위를 넘기지 않아야 한다.

AMQP 는 SMTP 와 달리 바이너리 프로토콜이다.
메시지 본문은 그대로 전달되고 마샬링, 언마샬링 과정에서 인코딩되거나 변환되지 않는다.
모든 메시지는 프로토콜 위반 걱정 없이 다양한 형식으로 전달될 수 있다.

```
MIME (Multipurpose Internet Mail Extensions)
이메일과 함께 동봉할 파일을 텍스트 문자로 전환해서 이메일 시스템을 통해 전달하기 위해 사용된다.
본래 목적은 위와 같지만 현재에는 웹 상에서 여러 형태의 파일 전달시 사용되고 있다.

바이너리 파일(이미지, 음악, 영상...) 전송시 바이너리를 텍스트로 변환하는 과정을 인코딩이라 하고 그 반대를 디코딩이라 한다.

MIME으로 인코딩 된 파일은 Content-type 에 인코딩에 대한 정보가 담겨진다.
```

```
SMTP (Simple Mail Transfer Protocol 25)
메일 전송시 사용되는 표준 프로토콜이다. (수신 프로토콜로는 POP3, IMAP 등이 있다.)
메일을 보내는 클라이언트는 SMTP 와 TCP 연결을 맺고 메일을 전송한다.
이런 연결지향형 프로토콜이기 때문에 SMTP 서버는 항상 연결을 위해 수신 대기한다.

```

content-type 과 content-encoding 속성을 통해 발행자와 소비자는 더 명확한 계약을 맺을 수 있다.
발행자 쪽에서 더 효율적인 방식으로 메시지 압축 방식을 바꿔야 하는 경우 content-encoding 도 함께 바뀔 것이다.
소비자 쪽에서는 content-encoding 을 검사하고 디코딩 할 수 없다면 메시지를 거부하는 방식으로 안전하게 처리할 수 있다.


#### message-id 와 correlation-id 를 이용한 메시지 참조
AMQP 스펙에서 `message-id` 와 `correlation-id`는 애플리케이션 용도로 지정하고 특별히 정의된 동작은 없다.
애플리케이션에서 원하는 용도로 사용할 수 있다.
두 필드는 255바이트의 UTF-8로 인코딩 된 값을 가진다.

message-id 는 메시지의 식별 값이다.

correlation-id 는 공식적인 정의는 없다.
좋은 사용 예로는 현재 메시지와 관련된 메시지의 message-id 를 correlation-id 로 지정해서 다른 메시지에 대한 응답 또는 관련됨을 표시할 수 있다.


#### expiration - 자동으로 메시지 만료하기
expiration 은 소비되지 않은 메시지를 버려야 할 때를 알려준다.
유닉스 시간으로 설정하며 문자열 형태로 저장되야 한다.
expiration 이 설정된 메시지가 RabbitMQ 에 도착하고 시간이 만료된 경우 메시지는 큐로 삽입되지 않고 삭제된다.

RabbitMQ 는 메시지가 아닌 큐에 만료시간을 지정할 수도 있다. `x-message-ttl`
위 속성이 설정된 큐에 메시지가 전달된 경우 ttl 이 만료되면 큐는 메시지를 버린다.


#### delivery-mode
delivery-mode 는 메시지를 소비자에게 전달하기 전에 디스크에 저장할지 여부를 결정한다. (디스크에 저장 O = 2, 디스크에 저장 X =1)
메시지를 디스크에 저장하는 경우 RabbitMQ 서버가 재시작되더라도 메시지가 소비될 때까지 큐에 남게 된다.

```
메시지 Persistence, 큐 Durable 의 차이

큐의 durable 속성: RabbitMQ 서버나 클러스터를 재시작한 후에도 큐에 대한 정의가 유지돼야 하는 지에 대한 설정이다.
메시지 Persistence (delivery-mode): 하나의 큐는 지속성 메시지(delivery-mode=2)와 비지속성 메시지(delivery-mode=1) 를 모두 포함할 수 있다.

??? - 큐는 durable false, 메시지는 비지속성이라면 ?? 
```

근본적으로 메모리 I/O 는 디스크 I/O 보다 빠르기 때문에 `delivery-mode` 를 1로 지정하는 것이 더 빠르게 메시지를 처리할 수 있다.