## Spring Neo4j

Neo4j 는 그래프 데이터베이스 관리 시스템이다.<br/>

그래프 데이터베이스? <br/>
그래프 데이터베이스는 어떤 관계를 저장하고 탐색하는데 좋은 데이터베이스이다.<br/>
그래프 데이터베이스는 노드를 사용해서 엔티티르 저장하고 그래프의 edge 로는 엔티티 간의 관계를 정의한다.<br/>
여기서 엣지는 항상 시작과 끝 노드를 가지고 유형과 방향을 가진다.<br/>
하나의 노드가 가질 수 있는 관계의 수와 종류에는 제한이 없다.<br/>

적합한 사용처
- 친구관계에 대한 탐색: a의 친구 b, b의 친구 c ...
- 추천 애플리케이션: 고객의 관심분야, 친구, 구매이력 등의 정보의 카테고리들을 그래프 관계로 정의할 수 있다.

neo4j 시작 및 설정
```shell
# 7474: http 프로토콜용
# 7687: bolt 프로토콜용 
version: '3'
services:
  neo4j:
    image: neo4j:latest
    ports:
      - 7474:7474
      - 7687:7687
    volumes:
      - ./conf/neo4j.conf:/conf/neo4j.conf
```

접근: http://localhost:7474/browser <br/>

초기 id/pw: neo4j/neo4j <br/>

```yaml
spring:
  data:
    neo4j:
      username: neo4j
      password: pass1234
```

---

### Cypher Query

DB 생성
```
create database dbName

:use dbName
```

노드 생성
```
create () // 빈 노드 생성

create(:NodeLableName) // single label

create(:testNode1:testNode11) // multiple label

create(tn:testNode1:testNode2) // alias

create(tn:testNode{data1: 'data1', data2: 'data2'}) return tn
```

조회
```
match(node) return node // 전체 노드 조회 (테스트시만 사용)

match(tn:testNode) return tn

match(tn:testNode1) return tn
match(tn:testNode1:testNode2) return tn

match(tn:testNode{data1: 'data1'}) return tn
match(tn:testNode{data1: 'data1', data2: 'data2'}) return tn limit 1


match(tn:testNode)
where tn.data1 = 'data1' and tn.data2 = 'data2' 
return tn

match(tn:testNode)
where tn.data1 in ['data1', 'data11']
return tn
```

---

### 참고

https://aws.amazon.com/ko/nosql/graph/ <br/>
https://www.youtube.com/watch?v=LjtcrWkC0-E
