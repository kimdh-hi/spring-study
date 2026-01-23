## springboot4 migration issue

3.5.x --> 4.0.1


### jpa entity column 네이밍 전략 관련 이슈
- column name 명시하지 않은 경우 snake case 가 아닌 camel case 로 생성됨.

#### hibernate naming strategy
- default 물리적 명명 전략: PhysicalNamingStrategySnakeCaseImpl
    - hibernate6 (springboot 3.x)
        - CamelCaseToUnderscoresNamingStrategy(SpringPhysicalNamingStrategy)
- default 논리적 명명 전략: SpringImplicitNamingStrategy
- https://docs.spring.io/spring-boot/how-to/data-access.html#howto.data-access.configure-hibernate-naming-strategy

```
IMPLICIT_NAMING_STRATEGY 논리적 명명 전략
- 해당 필드 이름이 그대로 사용
- @Column(name = "") 을 통해 커스텀 가능

//논리 이름: firstName
private String firstName;

//논리 이름: email
@Column(name = "email")
private String emailAddress; 
```

```
PHYSICAL_NAMING_STRATEGY 물리적 명명 전략
- 논리적 명명 전략에 의해 결정된 이름을 받아 실제 db 에 반영될 이름을 결정
- springboot 의 경우 snake case 로 변환하는 전략을 default 로 사용
- PhysicalNamingStrategySnakeCaseImpl(CamelCaseToUnderscoresNamingStrategy, SpringPhysicalNamingStrategy)

//논리 이름: firstName
//물리 이름: first_name
private String firstName;

//논리 이름: emailAddress
//물리 이름: email_address
@Column(name = "emailAddress")
private String email; 
```

hibernate6 물리적 명명 전략 적용 코드 일부
- 다른 조건 없이 대,소문자가 바뀌는 부분은 _ 대체하는 처리만 있음

```java
//CamelCaseToUnderscoresNamingStrategy
private Identifier apply(final Identifier name, final JdbcEnvironment jdbcEnvironment) {
	if ( name == null ) {
		return null;
	}
	StringBuilder builder = new StringBuilder( name.getText().replace( '.', '_' ) );
	for ( int i = 1; i < builder.length() - 1; i++ ) {
		if ( isUnderscoreRequired( builder.charAt( i - 1 ), builder.charAt( i ), builder.charAt( i + 1 ) ) ) {
			builder.insert( i++, '_' );
		}
	}
	return getIdentifier( builder.toString(), name.isQuoted(), jdbcEnvironment );
}
```

hibernate7 물리적 명명 전략 적용 코드 일부
- isQuoted ???
- unquotedIdentifier 호출되는 경우 hibernate6 와 동일하게 snake 변환 처리됨

```java
//PhysicalNamingStrategySnakeCaseImpl
private Identifier apply(final Identifier name) {
	if ( name == null ) {
		return null;
	}
	else if ( name.isQuoted() ) {
		return quotedIdentifier( name );
	}
	else {
		return unquotedIdentifier( name );
	}
}

private String camelCaseToSnakeCase(String name) {
	final StringBuilder builder = new StringBuilder( name.replace( '.', '_' ) );
	for ( int i = 1; i < builder.length() - 1; i++ ) {
		if ( isUnderscoreRequired( builder.charAt( i - 1 ), builder.charAt( i ), builder.charAt( i + 1 ) ) ) {
			builder.insert( i++, '_' );
		}
	}
	return builder.toString();
}

protected Identifier unquotedIdentifier(Identifier name) {
	return new Identifier( camelCaseToSnakeCase( name.getText() ).toLowerCase( Locale.ROOT ) );
}
```


Spring's naming strategy is no longer applied with globally_quoted_identifiers enabled
- https://github.com/spring-projects/spring-data-jpa/issues/4091

```
hibernate.globally_quoted_identifiers
- hibernate 가 사용하는 모든 식별자(테이블명, 컬럼명, 제약조건 등) 전부 큰따옴표로 감싼다.

hibernate.auto_quote_keyword
- 식별자 이름이 SQL 예약어와 충돌하는 경우에만 식별자를 큰따옴표로 감싼다.
```

예약어의 경우 snake case 변환에 해당하는 물리적 명명 전략이 적용되지 않는다.
예약어 중 snake case 적용 대상이 될만한 (대소문자로 구분되어 있는) 것은 없으므로 auto_quote_keyword 로 해결