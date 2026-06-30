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
@ActiveProfiles("replication")
class ReplicationProfileRoutingTest {
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
      val master = MySqlReplicationContainers.masterHostPort()
      val replica = MySqlReplicationContainers.replicaHostPort()
      registry.add("spring.datasource.url") {
        "jdbc:mysql:replication://$master,$replica/testdb"
      }
      registry.add("spring.datasource.username") { "root" }
      registry.add("spring.datasource.password") { "111111" }
    }
  }
}
