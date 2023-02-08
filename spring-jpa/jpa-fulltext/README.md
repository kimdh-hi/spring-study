## Jpa MySQL 전문검색

`%Like`, `%Like%` 와 같은 조건이 포함되는 쿼리는 정렬된 인덱스의 특성으로 인해 인덱스를 타지 못하고 `full scan` 이 발생한다.<br/>

요구사항에 따라 반드시 앞 쪽에 `%` 를 포함시켜야 할 수 있다. 이런 경우 데이터가 많아질수록 성능 저하가 발생한다.<br/>
이런 경우 `전문검색` 을 사용할 수 있다.

```sql
select ...
from ...
where MATCH(검색_대상_컬럼들) AGAINST(검색_키워드 in 검색_모드)
```

in boolean mode
- 키워드의 포함/불포함 여부 비교를 수행하고, 일치여부를 true/false 로 반환한다.
- 3개 연산자 사용 가능
  - `+` : AND 연산
  - `-` : NOT 연산
  - `연산자 없음` : OR 연산

fulltext 검색 글자수
- default=4
```
[기본설정 변경 - my.cnf]
ft_min_word_len=3
innodb_ft_min_token_size = 3
```


### 참고
https://hoing.io/archives/16853