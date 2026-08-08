package com.study.hierarchicalquery.tree.infra

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.study.hierarchicalquery.tree.domain.model.QTree.Companion.tree
import com.study.hierarchicalquery.tree.domain.model.Tree
import jakarta.persistence.EntityManager
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

  fun shiftDescendants(oldPrefix: String, newPrefix: String): Long {
    val shifted = query.update(tree)
      .set(tree.path, Expressions.asString(newPrefix).concat(tree.path.substring(oldPrefix.length)))
      .where(tree.path.startsWith(oldPrefix))
      .execute()

    entityManager.clear()

    return shifted
  }
}
