```
docker run -d -p 8888:8888 \
    --platform linux/amd64 \
    --name=config-server \
    -v /Users/daehyunkim/Documents/spring-study/spring-cloud/spring-cloud-bus:/config \
    -e SPRING_PROFILES_ACTIVE=native,cloud-bus-rabbit \
    -e SPRING_RABBITMQ_HOST=host.docker.internal \
    -e SPRING_RABBITMQ_USERNAME=guest \
    -e SPRING_RABBITMQ_PASSWORD=guest \
    -e SPRING_RABBITMQ_VIRTUAL_HOST=/ \
    --restart=always \
    hyness/spring-cloud-config-server
```

`-e MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=busrefresh \`

@ConfigurationProperties 갱신 안 됨 이슈
- https://github.com/spring-cloud/spring-cloud-kubernetes/issues/889
- `lateinit var`, `var + default value` 가 최선.. 