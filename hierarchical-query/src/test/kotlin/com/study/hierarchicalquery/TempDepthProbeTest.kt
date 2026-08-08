package com.study.hierarchicalquery

import com.study.hierarchicalquery.tree.application.TreeService
import jakarta.persistence.EntityManagerFactory
import org.hibernate.SessionFactory
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest
class TempDepthProbeTest {

  @Autowired private lateinit var treeService: TreeService

  @Autowired private lateinit var entityManagerFactory: EntityManagerFactory

  @Autowired private lateinit var jdbcTemplate: JdbcTemplate

  private val statistics get() = entityManagerFactory.unwrap(SessionFactory::class.java).statistics

  @Test
  fun probe() {
    listOf(2, 3, 5).forEach { branching ->
      jdbcTemplate.execute("set referential_integrity false")
      jdbcTemplate.execute("truncate table tree")
      jdbcTemplate.execute("set referential_integrity true")

      val root = treeService.create("r", null).id!!
      var level = listOf(root)
      var total = 1
      repeat(4) { d ->
        val next = mutableListOf<String>()
        level.forEach { parent ->
          repeat(branching) { i -> next += treeService.create("n$d-$i-${next.size}", parent).id!! }
        }
        total += next.size
        level = next
      }

      statistics.clear()
      treeService.findSubtreeNaive(root)
      val naive = statistics.prepareStatementCount

      statistics.clear()
      treeService.findSubtree(root)
      val path = statistics.prepareStatementCount

      statistics.clear()
      treeService.findSubtreeByCte(root)
      val cte = statistics.prepareStatementCount

      println("PROBE branching=$branching nodes=$total naive=$naive path=$path cte=$cte")
    }
  }
}
