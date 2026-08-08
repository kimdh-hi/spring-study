package com.study.hierarchicalquery.tree.infra

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.study.hierarchicalquery.tree.domain.model.QTree.Companion.tree
import com.study.hierarchicalquery.tree.domain.model.Tree
import jakarta.persistence.EntityManager
import org.hibernate.query.criteria.HibernateCriteriaBuilder
import org.springframework.stereotype.Repository

@Repository
class TreeQueryRepository(
  private val query: JPAQueryFactory,
  private val entityManager: EntityManager,
) {

  fun findDescendants(prefix: String): List<Tree> =
    query.selectFrom(tree)
      .where(tree.path.startsWith(prefix))
      .orderBy(tree.name.asc())
      .fetch()

  fun findSubtreeByCte(id: String): List<Tree> {
    val builder = entityManager.criteriaBuilder as HibernateCriteriaBuilder
    val criteria = builder.createQuery(Tree::class.java)

    val anchor = builder.createQuery(String::class.java)
    val start = anchor.from(Tree::class.java)
    anchor.select(start.get<String>(CTE_ID).alias(CTE_ID)).where(builder.equal(start.get<String>(CTE_ID), id))

    val subtree = criteria.withRecursiveUnionAll(anchor) { self ->
      val step = builder.createQuery(String::class.java)
      val child = step.from(Tree::class.java)
      val parent = child.join(self)
      parent.on(builder.equal(child.get<Tree>("parent").get<String>(CTE_ID), parent.get<String>(CTE_ID)))

      step.select(child.get<String>(CTE_ID).alias(CTE_ID))
    }

    val subtreeIds = criteria.subquery(String::class.java)
    subtreeIds.select(subtreeIds.from(subtree).get(CTE_ID))

    val node = criteria.from(Tree::class.java)

    return entityManager.createQuery(criteria.select(node).where(node.get<String>(CTE_ID).`in`(subtreeIds)))
      .resultList
  }

  fun shiftDescendants(oldPrefix: String, newPrefix: String): Long {
    val shifted = query.update(tree)
      .set(tree.path, Expressions.asString(newPrefix).concat(tree.path.substring(oldPrefix.length)))
      .where(tree.path.startsWith(oldPrefix))
      .execute()

    entityManager.clear()

    return shifted
  }

  companion object {
    private const val CTE_ID = "id"
  }
}
