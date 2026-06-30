package com.study.readwritesplitting

import com.study.readwritesplitting.application.UserService
import com.study.readwritesplitting.support.MySqlReplicationContainers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import kotlin.test.assertEquals

@SpringBootTest
@ActiveProfiles("routing")
class RoutingProfileRoutingTest {
  @Autowired
  private lateinit var userService: UserService

  @Test
  fun `readOnly 트랜잭션은 replica로 라우팅된다`() {
    val where = userService.whereAmIOnRead()
    assertEquals(2L, where.getServerId())
    assertEquals(1, where.getReadOnly())
  }

  @Test
  fun `write 트랜잭션은 master로 라우팅된다`() {
    val where = userService.whereAmIOnWrite()
    assertEquals(1L, where.getServerId())
    assertEquals(0, where.getReadOnly())
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
