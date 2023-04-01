## Spring boot external properties

---

#### 프로필 우선순위
지정하지 않은 경우 default 프로필 활성화 <br/>

프로필은 기본적으로 `위에서 아래로 적용`된다. <br/>
동일한 key-value를 가지는 문서가 있고 `두 개 프로필을 지정`하는 경우 `ex) --spring.profiles.active=dev,prod` <br/>
결과적으로 `더 아래쪽에 위치한` 프로퍼티가 적용된다.<br/>

만약 가장 아래쪽에 default 프로필로 어떤 프로퍼티가 설정되었다면 위 쪽의 어떤 default 프로필을 포함한 어떤 프로필이 사용됐더라도 덮어쓰여진다. <br/>

default 프로필에 대한 프로퍼티는 가장 위 쪽에 모아서 작성하는 것이 좋다.

---

#### 프로필 지정
```
[Program arguments]
--spring.profiles.active=dev

[VM options]
-Dspring.profiles.active=dev
```