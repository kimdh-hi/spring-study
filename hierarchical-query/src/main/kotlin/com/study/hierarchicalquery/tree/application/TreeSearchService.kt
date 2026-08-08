package com.study.hierarchicalquery.tree.application

import com.study.hierarchicalquery.tree.domain.model.Tree
import com.study.hierarchicalquery.tree.domain.model.TreeView
import com.study.hierarchicalquery.tree.domain.repository.TreeRepository
import com.study.hierarchicalquery.tree.infra.TreeQueryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TreeSearchService(
  private val treeRepository: TreeRepository,
  private val treeQueryRepository: TreeQueryRepository,
) {

  fun findSubtreeByRecursion(id: String): TreeView = toView(findNode(id))

  fun findSubtreeByLevelIn(id: String): TreeView {
    val root = findNode(id)
    val nodes = mutableListOf(root)
    var parentIds = listOf(root.id!!)

    while (parentIds.isNotEmpty()) {
      val children = treeRepository.findAllByParentIdIn(parentIds)
      nodes += children
      parentIds = children.map { it.id!! }
    }

    return TreeView.assemble(nodes).single()
  }

  fun findSubtreeByMaterializedPath(id: String): TreeView {
    val node = findNode(id)
    val descendants = treeQueryRepository.findDescendants(node.descendantPrefix)

    return TreeView.assemble(listOf(node) + descendants).single()
  }

  fun findSubtreeByRecursiveCte(id: String): TreeView =
    assembleRoot(treeRepository.findSubtreeByCte(id), id)

  fun findSubtreeByCriteriaCte(id: String): TreeView =
    assembleRoot(treeQueryRepository.findSubtreeByCte(id), id)

  private fun findNode(id: String): Tree =
    treeRepository.findByIdOrNull(id) ?: throw NoSuchElementException("tree not found. id=$id")

  private fun assembleRoot(nodes: List<Tree>, id: String): TreeView =
    TreeView.assemble(nodes).singleOrNull() ?: throw NoSuchElementException("tree not found. id=$id")

  private fun toView(node: Tree): TreeView = TreeView(
    id = node.id!!,
    name = node.name,
    depth = node.depth,
    path = node.path,
    children = node.children.sortedBy { it.name }.map(::toView),
  )
}
