## Spring data envers

envers 이전에는 별도 history 테이블을 만들고 CUD 연산시 해당 history 테이블을 갱신해줘야 했다.<br/>
envers 는 데이터 변경에 대해 추가적인 로깅작업을 쉽게 할 수 있도록 지원한다.


### `@Audited`
- 변경이력을 추적할 엔티티의 클래스 레벨에 추가
- `엔티티_이름_prefix` 히스토리 테이블 생성

### rev_type
- `0`: insert
- `1`: update
- `2`: delete

### 참고
https://sehajyang.github.io/2020/04/15/springboot-envers-logging-for-revision/