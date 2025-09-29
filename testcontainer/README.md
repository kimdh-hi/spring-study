## testcontainer

#### @ServiceConnection
- springboot 3.1 이전 testcontainer 로 띄운 서비스에 대해 @DynamicPropertySource 를 통해 필요 옵션값을 설정
- @Container 에 @ServiceConnection 추가하는 경우 필요 옵션값을 기본값으로 자동설정
  - mysql: MySQLContainer (static final String DEFAULT_USER = "test";)
  - redis: RedisContainerConnectionDetails

#### setting issue (rancher desktop)

```
ould not find a valid Docker environment. Please see logs and check configuration
java.lang.IllegalStateException: Could not find a valid Docker environment. Please see logs and check configuration
	at org.testcontainers.dockerclient.DockerClientProviderStrategy.lambda$getFirstValidStrategy$7(DockerClientProviderStrategy.java:274)
	at java.base/java.util.Optional.orElseThrow(Optional.java:403)
	at org.testcontainers.dockerclient.DockerClientProviderStrategy.getFirstValidStrategy(DockerClientProviderStrategy.java:265)
	at org.testcontainers.DockerClientFactory.getOrInitializeStrategy(DockerClientFactory.java:154)
	at org.testcontainers.DockerClientFactory.client(DockerClientFactory.java:196)
	at org.testcontainers.DockerClientFactory$1.getDockerClient(DockerClientFactory.java:108)
	at com.github.dockerjava.api.DockerClientDelegate.authConfig(DockerClientDelegate.java:109)
	at org.testcontainers.containers.GenericContainer.start(GenericContainer.java:321)
	at org.testcontainers.junit.jupiter.TestcontainersExtension$StoreAdapter.start(TestcontainersExtension.java:276)
	at org.testcontainers.junit.jupiter.TestcontainersExtension$StoreAdapter.access$200(TestcontainersExtension.java:263)
	at org.testcontainers.junit.jupiter.TestcontainersExtension.lambda$startContainers$4(TestcontainersExtension.java:83)
	at org.testcontainers.junit.jupiter.TestcontainersExtension.lambda$startContainers$5(TestcontainersExtension.java:83)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
	at org.testcontainers.junit.jupiter.TestcontainersExtension.startContainers(TestcontainersExtension.java:83)
	at org.testcontainers.junit.jupiter.TestcontainersExtension.beforeAll(TestcontainersExtension.java:57)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
	Suppressed: org.testcontainers.containers.ContainerFetchException: Can't get Docker image: RemoteDockerImage(imageName=mysql:latest, imagePullPolicy=DefaultPullPolicy(), imageNameSubstitutor=org.testcontainers.utility.ImageNameSubstitutor$LogWrappedImageNameSubstitutor@6ac4944a)
		at org.testcontainers.containers.GenericContainer.getDockerImageName(GenericContainer.java:1364)
```

- https://github.com/rancher-sandbox/rancher-desktop/issues/2534?timeline_page=1
  - rancher desktop > preference > application  general > Administrative access check enable
