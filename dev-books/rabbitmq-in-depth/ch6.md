## ch6
익스체인지 라우팅을 통한 메시지 패턴

RabbitMQ의 최대 강점은 발행자 애플리케이션이 제공한 라우팅 정보를 기반으로 서로 다른 큐로 유연하게 메시지를 라우팅할 수 있다는 점이다.

- Direct exchange
- Fanout exchange
- Topic exchange
- Headers exchange

#### Direct exchange
Direct exchange 는 특정 큐, 특정 큐 그룹에 메시지를 전달할 때 사용할 수 있다.
메시지 발행시 사용하는 `라우팅 키`와 동일한 키로 익스체인지에 바인딩된 모든 큐에 메시지를 전달한다.

동일한 라우팅 키를 사용하여 여러 큐를 한 개 다이렉트 익스체인지에 바인딩할 수 있다.


#### Fanout exchange
Direct exchange의 경우 라우팅 키를 기반으로 특정 큐에 메시지를 전달하는 반면 `Fanout exchange` 는 바인딩 된 모든 큐로 메시지를 전달한다.
라우팅 키를 평가하지 않기 때문에 성능상 이점이 있다.


#### Topic exchange
`Topic exchange` 는 Direct exchange 와 유사하게 라우팅 키를 기반으로 큐를 식별하고 메시지를 라우팅한다.
Direct exchange 와 다른 점은 라우팅 키에 와일드카드 기반 패턴 매칭을 사용할 수 있다는 것이다.

외일드 카드
- `*` : 하나의 단어로 대체된다.
- `#` : 0개 이상의 단어로 대체된다.

`image.new.profile`
- `image`: 메시지 분류
- `new`: 메시지 유형
- `profile`: 추가 분류 형식

`image.new.#` : 추가 분류 형식에 관계없이 모든 새로운 이미지를 처리한다.
`#.profile` : 추가 분류 형식이 `profile` 인 모든 메시지를 수신한다.
`image.delete.*` : 메시지 유형이 `delete` 인 모든 메시지를 수신한다.

`Topic exchange` 의 와일드카드 기반 패턴매칭을 사용하면 Fanout-exchange 도 Direct-exchange 도 똑같이 만들 수 있다.


#### Header exchange
`Header exchange` 는 메시지 속성 중 `headers` 테이블을 사용해서 RabbitMQ에서 특정한 규칙의 라우팅을 처리한다.

`Header exchange`에 연결하는 큐는 key-value 쌍의 배열과 `x-match` 를 사용한다.
`x-match` 는 문자열로 `any` 또는 `all` 로 값을 설정한다.

`x-match=any` : 헤더 테이블 값 중 하나만 일치하면 메시지가 전달된다.
`x-match=all` : 인수로 전달된 모든 값이 일치해야 메시지를 전달한다.

발행자 측에서 메시지와 함께 정의한 헤더 테이블이 전달된다.

소비자 측은 exchange 에 큐를 바인딩 할 때 헤더 테이블에 매칭될 key-value 인수를 지정한다.
소비자는 key-value 와 x-match 를 지정하면서 발행자의 헤더 테이블과 어떤 식으로 매칭될지를 추가로 설정한다.

```
발행자
{
	'headers': {
		'key1': 'value1',
		'key2': 'value2',
		'key3': 'value3'
	}
}

소비자
{
	arguments: {
		'x-match': 'all'
		'key1': 'value1',
		'key2': 'value2',
		'key3': 'value3'
	}
}
x-match가 all이므로 모든 key-value가 같아야 함 따라서 위 발행자가 발행한 메시지와 매칭됨


소비자
{
	arguments: {
		'x-match': 'any'
		'key1': 'value1',
		'key2': 'other-value2'
	}
}
x-match가 any이므로 한 개 key-value 만 매칭되도 됨 따라서 위 발행자가 발행한 메시지와 매칭됨
```

`Header exchange` 고려 사항
`Header exchange` 는 any, all 을 추가 인자로 제공하면서 보다 유연한 라우팅 규칙을 만들 수 있다.
하지만 추가적인 계산을 위한 오버헤드가 따른다.
`Header exchange`로 메시지를 라우팅할 때 값을 평가하기 전에 헤더의 모든 값을 키를 기준으로 정렬한다.
이런 추가적인 작업 때문에 다른 익스체인지에 비해 `Header exchange`가 상당히 느리다고 알려져있다. (근데 큰 차이가 없다고 한다.. ㅋㅋ)


#### 익스체인지 간 라우팅하기
AMQP 스펙에는 없지만 RabbitMQ 는 익스체인지의 조합으로 메시지를 라우팅하는 것이 가능하다.
큐를 익스체인지가 바인딩하는 일반적인 구조가 아니고 익스체인지를 다른 익스체인지에 바인딩하는 것이다.

익스체인지 간 바인딩에 사용되는 라우팅 로직은 큐와 익스체인지 간과 동일하다.

but, 추가적인 오버헤드와 복잡도가 발생한다. 이 점을 고려하자.


#### Consistent hashing exchange
`Consistent hashing exchange` 는 익스체인지에 메시지를 발행할 때 가중치를 조정하는 용도로 사용된다.
기존 라우팅 키, 헤더 테이블을 기반으로 큐를 결정하는 것과 달리 `정수 기반 가중치`를 사용해서 큐를 설정하고 메시지를 전달한다.

Consistent hashing exchange 는 가중치 기반으로 연결된 모든 큐 중 하나의 큐로만 메시지를 전달한다.
Consistent hashing exchange 를 사용하면 추가적인 작업 없이 연결된 모든 큐에 설정된 가중치만큼 메시지를 전달할 수 있다.

가중치는 Consistent hashing exchange에 큐를 바인딩 할 때 설정할 수 있다.
