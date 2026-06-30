package com.study.readwritesplitting

import com.study.readwritesplitting.application.UserService
import com.study.readwritesplitting.support.MySqlReplicationContainers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import javax.sql.DataSource
import kotlin.test.assertEquals

@SpringBootTest(properties = ["spring.main.allow-bean-definition-overriding=true"])
@ActiveProfiles("routing")
@Import(NoLazyProxyRoutingTest.NoLazyProxyConfig::class)
class NoLazyProxyRoutingTest {
  @Autowired
  private lateinit var userService: UserService

  @Test
  fun `LazyProxy 없으면 readOnly 읽기가 master로 샌다`() {
    val where = userService.whereAmIOnRead()
    assertEquals(1L, where.getServerId())
  }

  @TestConfiguration
  class NoLazyProxyConfig {
    @Bean
    @Primary
    fun dataSource(
      @Qualifier("routingDataSource") routingDataSource: DataSource,
    ): DataSource = routingDataSource
  }

  companion object {
    @JvmStatic
    @DynamicPropertySource
    fun props(registry: DynamicPropertyRegistry) {
      registry.add("datasource.writer.jdbc-url") {
        "jdbc:mysql://${MySqlReplicationContainers.masterHostPort()}/testdb"
      }
      registry.add("datasource.writer.username") { "root" }
      registry.add("datasource.writer.password") { "111111" }
      registry.add("datasource.reader.jdbc-url") {
        "jdbc:mysql://${MySqlReplicationContainers.replicaHostPort()}/testdb"
      }
      registry.add("datasource.reader.username") { "root" }
      registry.add("datasource.reader.password") { "111111" }
    }
  }
}
