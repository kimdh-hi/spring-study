package com.toy.springstomp.support

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.images.builder.Transferable
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

private const val AMQP_PORT = 5672
private const val STOMP_PORT = 61613

@Testcontainers
abstract class RabbitTestContainer {
  companion object {
    @Container
    @JvmStatic
    val rabbit: GenericContainer<*> = GenericContainer(DockerImageName.parse("rabbitmq:4.1-alpine"))
      .withCopyToContainer(Transferable.of("[rabbitmq_stomp]."), "/etc/rabbitmq/enabled_plugins")
      .withExposedPorts(AMQP_PORT, STOMP_PORT)

    @DynamicPropertySource
    @JvmStatic
    fun rabbitProperties(registry: DynamicPropertyRegistry) {
      registry.add("spring.rabbitmq.host") { rabbit.host }
      registry.add("spring.rabbitmq.port") { rabbit.getMappedPort(AMQP_PORT) }
      registry.add("stomp.relay-port") { rabbit.getMappedPort(STOMP_PORT) }
    }
  }
}
