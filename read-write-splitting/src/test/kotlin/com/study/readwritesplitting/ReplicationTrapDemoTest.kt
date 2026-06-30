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
class ReplicationTrapDemoTest {
  @Autowired
  private lateinit var userService: UserService

  @Test
  fun `handling_mode=AFTER_TRANSACTION 이면 readOnly 읽기가 master로 샌다`() {
    val where = userService.whereAmIOnRead()
    assertEquals(1L, where.getServerId())
    assertEquals(0, where.getReadOnly())
  }

  companion object {
    @JvmStatic
    @DynamicPropertySource
    fun props(registry: DynamicPropertyRegistry) {
      val master = MySqlReplicationContainers.masterHostPort()
      val replica = MySqlReplicationContainers.replicaHostPort()
      registry.add("spring.datasource.url") { "jdbc:mysql:replication://$master,$replica/testdb" }
      registry.add("spring.datasource.username") { "root" }
      registry.add("spring.datasource.password") { "111111" }
      registry.add("spring.jpa.properties.hibernate.connection.handling_mode") {
        "DELAYED_ACQUISITION_AND_RELEASE_AFTER_TRANSACTION"
      }
    }
  }
}
