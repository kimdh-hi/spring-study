## Spring AI

### pgvector vector store, embedding setting

```shell
docker pull pgvector/pgvector:pg18
```

```shell
docker run -d --name pgvector \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD=postgres \
-p 5432:5432 \
-v pgdata:/var/lib/postrgresql/data \
pgvector/pgvector:pg18
```

```kotlin
implementation("org.springframework.ai:spring-ai-starter-vector-store-pgvector")
```

```yaml
spring:
  ai:
    vectorstore:
      pgvector:
        initialize-schema: true
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: postgres
```



---

## reference 
- https://docs.spring.io/spring-ai/reference/index.html
