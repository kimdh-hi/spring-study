package com.study.hierarchicalquery.tree.application

import com.study.hierarchicalquery.tree.domain.model.Tree
import com.study.hierarchicalquery.tree.domain.repository.TreeRepository
import com.study.hierarchicalquery.tree.infra.TreeQueryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TreeService(
  private val treeRepository: TreeRepository,
  private val treeQueryRepository: TreeQueryRepository,
) {

  fun create(name: String, parentId: String?): Tree =
    treeRepository.save(Tree(name = name, parent = parentId?.let(::findNode)))

  fun moveSubtree(id: String, newParentId: String?): Tree {
    val node = findNode(id)
    val oldPrefix = node.descendantPrefix

    node.moveTo(newParentId?.let(::findNode))
    treeRepository.flush()
    treeQueryRepository.shiftDescendants(oldPrefix, node.descendantPrefix)

    return findNode(id)
  }

  private fun findNode(id: String): Tree =
    treeRepository.findByIdOrNull(id) ?: throw NoSuchElementException("tree not found. id=$id")
}
