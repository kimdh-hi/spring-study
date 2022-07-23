## Jmeter

### 실행

```bash
/opt/homebrew/Cellar/jmeter/5.5/bin/jmeter
```

### Thread Group

- Number of Threads(users): 몇 개의 Thread(사용자)가 동시에 접속하는지
- Loop count: Number of Thread 의 접속 횟수

HttpRequest

- ThreadGroup - add - Sampler - Http Request
- 한 개 Thread Group 내 여러 개 HttpRequest 생성 가능

Header 추가

- Thread Group - add - Config Elements - Http Header Manager - add