## Spring ElasticSearch

Json 문서 형식의 데이터를 Logstash 등 수집 도구나 API 를 사용해서 ElasticSearch 로 전송할 수 있다.<br/>

ElasticSearch 는 문서를 저장하고 인덱스에 문서에 대한 검색 가능한 참조를 추가한다.<br/>

ElasticSearch 가 제공하는 API 또는 Kibana 등을 사용해서 데이터를 조회하고 시각화 할 수 있다.<br/>

### 주요기능 
- 실시간 분석
  - 현재 대용량 데이터 분석에 널리 사용되는 것은 하둡의 Pig, Hive 와 같은 맵 리듀서들이다.
  - ElasticSearch 는 하둡 시스템과 달리 ElasticSearch 클러스터가 실행되는 동안 계속해서 데이터가 입력되고, 그와 동시에 실시간에 가까운 속도로 색인된 데이터의 검색이 가능하다.
- 전문 검색 엔진
  - ElasticSearch 는 색인된 모든 데이터를 `역파일 색인 구조(역색인 구조)`로 저장하여 가공된 텍스트를 검색한다.
  - key-value 형식이 아닌 문서 기반이기 때문에 복하벅인 정보를 포함하는 형식의 문서를 그대로 저장할 수 있고 직관저긍로 이해하고 사용할 수 있다.
  - 다만, Json 이 ElasticSearch 가 지원하는 유일한 형식이기 때문에 사전에 입력할 데이터를 JSON 형식으로 가공하는 것이 필요하다. (=> `Logstash`)  

### 역색인
색인의 경우 책의 목차에 해당한다.<br/>
역색인의 경우 책 뒷 편의 찾아보기에 해당한다.<br/>

역색인의 경우 색인보다 검색에 있어 매우 빠르다.<br/>

색인을 통해 검색하는 것은 RDBMS 에서 검색하는 것과 동일하다.<br/>
`like` 연산을 통해 모든 row 를 지나면서 키워드가 있는지 확인하다.<br/>

역색인의 경우 역색인 검색을 위해 검색 엔진에서 추출된 키워드에 매핑되는 여러 문서를 구성한다. <br/>
특정 키워드를 검색하면 매핑된 모든 문서의 id 를 얻을 수 있는 것이다.



### 참고
https://aws.amazon.com/ko/what-is/elasticsearch/
https://esbook.kimjmin.net/01-overview/1.1-elastic-stack/1.1.1-elasticsearch
https://steady-coding.tistory.com/581
https://tecoble.techcourse.co.kr/post/2021-10-19-elasticsearch/