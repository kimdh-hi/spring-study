## Spring flyway

### Flyway

Flyway 는 DB 마이그레이션 툴로 DB 형상관리에 사용된다.<br/>

로컬, 개발환경이 아닌 경우 데이터가 쌓여있는 서비스에서 스키마를 변경해야 하는 경우 flyway 를 사용하지 않는다면 직접 DB 서버에 스키마 변경에 대한 DDL 을 날려야 한다.<br/>
추가로 DDL 변경 이력을 쌓으므로 이를 통해 변경사항을 추적할 수 있다.<br/>


### 네이밍 컨벤션
flyway 스크립트 파일의 이름은 3개 영역으로 구성된다.<br/>
Prefix, Version, Description <br/>
<br/>

Prefix
- V, U, R 세 개 prefix 를 지원한다.
- V 는 새로운 버전으로 업데이트 하는 경우
- U 는 현재 버전을 이전 버전으로 되돌리는 경우
- R 은 버전과 관계없이 매 번 실행하는 경우

Version
- Prefix 중 V와 U 는 Version 을 필요로 한다.
- 새로운 스크립트르 작성한다면 스크립트의 버전은 이 전 스크립트보다 높아야 한다.
- 만약 더 낮은 버전의 스크립트를 추가한다면 flyway 는 이를 무시한다.
- 버전은 1.0, 2.0 과 같은 형태일수도 있고 20220221 과 같은 날짜 형태일수도 있다.

Description
- 스크립트의 내용을 표현한다. (띄어쓰기가 필요하다면 언더바 한 개를 사용한다.)

주의
- version 과 description 사이는 반드시 언더바(_) 가 두 개여야 한다.
- version 이 없다면 Prefix 와 description 사이 또한 언더바(_) 가 두 개여야 한다.

### 참고
https://tecoble.techcourse.co.kr/post/2021-10-23-flyway/