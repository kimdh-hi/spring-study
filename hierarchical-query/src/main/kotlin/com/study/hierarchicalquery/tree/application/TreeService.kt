package com.study.hierarchicalquery.tree.application

import com.study.hierarchicalquery.tree.domain.model.Tree
import com.study.hierarchicalquery.tree.domain.model.TreeView
import com.study.hierarchicalquery.tree.domain.repository.TreeRepository
import com.study.hierarchicalquery.tree.infra.TreeQueryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TreeService(
  private val treeRepository: TreeRepository,
  private val treeQueryRepository: TreeQueryRepository,
) {

  @Transactional
  fun create(name: String, parentId: String?): Tree {
    val parent = parentId?.let {
      treeRepository.findByIdOrNull(it) ?: throw NoSuchElementException("tree not found. id=$it")
    }

    return treeRepository.save(Tree(name = name, parent = parent))
  }

  @Transactional(readOnly = true)
  fun findSubtreeByLevel(id: String): TreeView {
    val root = treeRepository.findByIdOrNull(id) ?: throw NoSuchElementException("tree not found. id=$id")
    val nodes = mutableListOf(root)
    var parentIds = listOf(root.id!!)

    while (parentIds.isNotEmpty()) {
      val children = treeRepository.findAllByParentIdIn(parentIds)
      nodes += children
      parentIds = children.map { it.id!! }
    }

    return TreeView.assemble(nodes).single()
  }

  //Materialized Path 방식
  @Transactional(readOnly = true)
  fun findSubtree(id: String): TreeView {
    val node = treeRepository.findByIdOrNull(id) ?: throw NoSuchElementException("tree not found. id=$id")
    val descendants = treeQueryRepository.findDescendants(node.descendantPrefix)

    return TreeView.assemble(listOf(node) + descendants).single()
  }

  @Transactional(readOnly = true)
  fun findSubtreeByCte(id: String): TreeView {
    val nodes = treeRepository.findSubtreeByCte(id)

    return TreeView.assemble(nodes).single()
  }

  @Transactional(readOnly = true)
  fun findSubtreeNaive(id: String): TreeView {
    val node = treeRepository.findByIdOrNull(id) ?: throw NoSuchElementException("tree not found. id=$id")

    return walk(node)
  }

  @Transactional(readOnly = true)
  fun findAncestors(id: String): List<Tree> {
    val node = treeRepository.findByIdOrNull(id) ?: throw NoSuchElementException("tree not found. id=$id")
    val ancestors = treeRepository.findAllById(node.ancestorIds)

    return ancestors.sortedBy { it.depth }
  }

  @Transactional
  fun moveSubtree(id: String, newParentId: String?): Tree {
    val node = treeRepository.findByIdOrNull(id) ?: throw NoSuchElementException("tree not found. id=$id")
    val oldPrefix = node.descendantPrefix
    val newParent = newParentId?.let {
      treeRepository.findByIdOrNull(it) ?: throw NoSuchElementException("tree not found. id=$it")
    }

    node.moveTo(newParent)
    treeRepository.flush()
    treeQueryRepository.shiftDescendants(oldPrefix, node.descendantPrefix)

    return treeRepository.findByIdOrNull(id) ?: throw NoSuchElementException("tree not found. id=$id")
  }

  private fun walk(node: Tree): TreeView = TreeView(
    id = node.id!!,
    name = node.name,
    depth = node.depth,
    path = node.path,
    children = node.children.sortedBy { it.name }.map(::walk),
  )
}
