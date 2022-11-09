## Jenkins

```yaml
version: '3.9'
services:
  jenkins:
  	image: jenkins/jenkins:latest
  	container_name: jenkins
  	envrinment:
  	  - "TZ=Asia/Seoul"
  	ports:
      - "8888:8080"
    volumes:
      - "./jenkins_data:/var/jenkins_home"
```

초기 비밀번호
```
/var/jenkins_home/secrets
```

---

## Jenkins pipeline

```
[스크립트 기반 Jenkins Pipeline]
node {
	stage('Build') {

	}
	stage('Test') {

	}
	stage('Deploy') {

	}
}
```

```
[선언적 Jenkins Pipeline]
pipeline {
	agent any
	stages {
		stage('Build') {
			steps {

			}
		}
		stage('Test') {
			steps {

			}

		}
		stage('Deploy') {
			steps {

			}

		}
	}
}
```

`agent`
- pipeline 바로 아래 지정되는 `agent` 
  - Job이 어떤 Node 에서 실행될지 지정
- stage 에 지정되는 `agent`
  - 해당 stage 에만 적용되는 Node
- **agent** 종류: `none`, `any`, `label`, `docker`, `dockerfile`, `kubernetes`    

`post`
- stages 혹은 stage 의 작업이 끝난 후의 동작을 지정
- stages 이후에 있는 경우 stages 가 끝난 후, stages 내부에 있는 경우 위치에 따라 이전 stage 가 종료된 후 호출된다.
- `post` 조건
  - always: 항상 실행
  - changes
  - fixed
  - aborted
  - success : 성공시 실행
  - failure : 실패시 실행
  - unsuccessful
  - cleanup
  - unstable

`environment`
- key-value 형태
- pipeline 내부에서 사용될 환경변수 지정
- credentials 를 통해 Jenkins 에 지정한 credential 에 접근  

`parameters`
- 파이프라인 trigger 시 입력받는 변수 지정
- type: string, text, booleanParam, choice, password


































 
