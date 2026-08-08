package com.study.hierarchicalquery.tree.domain.model

data class TreeView(
  val id: String,
  val name: String,
  val depth: Int,
  val path: String,
  val children: List<TreeView>,
) {
  companion object {
    fun assemble(nodes: List<Tree>): List<TreeView> {
      val byParent = nodes.groupBy { it.parentId }
      val present = nodes.mapTo(mutableSetOf()) { it.id }

      fun build(node: Tree): TreeView = TreeView(
        id = node.id!!,
        name = node.name,
        depth = node.depth,
        path = node.path,
        children = byParent[node.id].orEmpty().sortedBy { it.name }.map(::build),
      )

      return nodes.filter { it.parentId !in present }
        .sortedBy { it.name }
        .map(::build)
    }
  }
}
