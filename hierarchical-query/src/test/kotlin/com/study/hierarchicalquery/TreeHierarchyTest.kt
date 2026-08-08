package com.study.hierarchicalquery

import com.study.hierarchicalquery.tree.application.TreeService
import com.study.hierarchicalquery.tree.domain.model.TreeView
import jakarta.persistence.EntityManagerFactory
import org.hibernate.SessionFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@SpringBootTest
class TreeHierarchyTest {

  @Autowired private lateinit var treeService: TreeService

  @Autowired private lateinit var entityManagerFactory: EntityManagerFactory

  @Autowired private lateinit var jdbcTemplate: JdbcTemplate

  private lateinit var ids: Map<String, String>

  private val statistics get() = entityManagerFactory.unwrap(SessionFactory::class.java).statistics

  @BeforeEach
  fun setUp() {
    jdbcTemplate.execute("set referential_integrity false")
    jdbcTemplate.execute("truncate table tree")
    jdbcTemplate.execute("set referential_integrity true")
    ids = seed()
  }

  @Test
  @DisplayName("인접 리스트 재귀 순회는 노드 수만큼 쿼리를 발행한다")
  fun naiveSubtreeCausesNPlusOne() {
    val (tree, queries) = countQueries { treeService.findSubtreeNaive(ids.getValue("r")) }

    assertEquals(TOTAL_NODES, flatten(tree).size)
    assertEquals(TOTAL_NODES + 1L, queries)
  }

  @Test
  @DisplayName("레벨 단위 in 조회는 노드 수가 아니라 깊이에 비례한다")
  fun levelSubtreeScalesWithDepth() {
    val (tree, queries) = countQueries { treeService.findSubtreeByLevel(ids.getValue("r")) }

    assertEquals(TOTAL_NODES, flatten(tree).size)
    assertEquals(5L, queries)
  }

  @Test
  @DisplayName("materialized path 는 노드 수와 무관하게 조회 1 + 시작 노드 조회 1 로 끝난다")
  fun pathSubtreeUsesFixedQueryCount() {
    val (tree, queries) = countQueries { treeService.findSubtree(ids.getValue("r")) }

    assertEquals(TOTAL_NODES, flatten(tree).size)
    assertEquals(2L, queries)
  }

  @Test
  @DisplayName("recursive CTE 도 1 쿼리로 같은 서브트리를 반환한다")
  fun cteSubtreeMatchesPathSubtree() {
    val (tree, queries) = countQueries { treeService.findSubtreeByCte(ids.getValue("a")) }

    assertEquals(1L, queries)
    assertEquals(
      flatten(treeService.findSubtree(ids.getValue("a"))).map { it.id }.toSet(),
      flatten(tree).map { it.id }.toSet(),
    )
  }

  @Test
  @DisplayName("조상 조회는 path 파싱으로 재귀 없이 끝난다")
  fun ancestorsAreResolvedFromPath() {
    val (ancestors, queries) = countQueries { treeService.findAncestors(ids.getValue("a1x")) }

    assertEquals(listOf("r", "a", "a1"), ancestors.map { it.name })
    assertEquals(2L, queries)
  }

  @Test
  @DisplayName("@UuidGenerator + persist 로 INSERT 1 번만 발행한다")
  fun createIssuesSingleInsert() {
    val (_, queries) = countQueries { treeService.create("standalone", null) }

    assertEquals(1L, queries)
  }

  @Test
  @DisplayName("서브트리 이동은 자손 전체의 path 와 depth 를 bulk UPDATE 1 번으로 갱신한다")
  fun moveSubtreeShiftsAllDescendants() {
    val moved = treeService.moveSubtree(ids.getValue("a"), ids.getValue("b"))

    assertEquals(2, moved.depth)

    val subtree = flatten(treeService.findSubtree(ids.getValue("a")))
    assertEquals(7, subtree.size)
    assertTrue(subtree.all { it.path.startsWith(moved.path) })
    assertEquals(4, subtree.single { it.name == "a1x" }.depth)
    assertEquals(listOf("r", "b", "a"), treeService.findAncestors(ids.getValue("a1")).map { it.name })
  }

  @Test
  @DisplayName("자기 자손 밑으로 이동하면 순환이 되므로 거부한다")
  fun moveUnderOwnDescendantIsRejected() {
    assertFailsWith<IllegalArgumentException> {
      treeService.moveSubtree(ids.getValue("a"), ids.getValue("a1x"))
    }
  }

  private fun <T> countQueries(block: () -> T): Pair<T, Long> {
    statistics.clear()
    val result = block()

    return result to statistics.prepareStatementCount
  }

  private fun flatten(view: TreeView): List<TreeView> =
    listOf(view) + view.children.flatMap(::flatten)

  private fun seed(): Map<String, String> {
    val created = mutableMapOf<String, String>()
    created["r"] = treeService.create("r", null).id!!

    listOf("a", "b").forEach { first ->
      created[first] = treeService.create(first, created["r"]).id!!

      listOf("1", "2").forEach { second ->
        val child = "$first$second"
        created[child] = treeService.create(child, created[first]).id!!

        listOf("x", "y").forEach { third ->
          val grandChild = "$child$third"
          created[grandChild] = treeService.create(grandChild, created[child]).id!!
        }
      }
    }

    return created
  }

  companion object {
    private const val TOTAL_NODES = 15
  }
}
