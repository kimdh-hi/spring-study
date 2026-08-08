package com.study.hierarchicalquery.tree.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(
  name = "tree",
  indexes = [Index(columnList = "path"), Index(columnList = "parent_id")],
)
class Tree(
  @Id
  @UuidGenerator
  @Column(name = "id", nullable = false, updatable = false)
  var id: String? = null,

  @Column(nullable = false)
  var name: String,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  var parent: Tree? = null,

) {

  @Column(nullable = false, length = PATH_MAX_LENGTH)
  var path: String = pathUnder(parent)

  @OneToMany(mappedBy = "parent")
  val children: MutableList<Tree> = mutableListOf()

  val ancestorIds: List<String>
    get() = path.split(SEPARATOR).filter(String::isNotEmpty)

  val parentId: String?
    get() = ancestorIds.lastOrNull()

  val depth: Int
    get() = ancestorIds.size

  val descendantPrefix: String
    get() = path + id!! + SEPARATOR

  fun moveTo(newParent: Tree?) {
    require(newParent == null || (newParent.id != id && !newParent.path.startsWith(descendantPrefix))) {
      "cannot move under itself or its descendant"
    }

    parent = newParent
    path = pathUnder(newParent)
  }

  companion object {
    const val PATH_MAX_LENGTH = 1000
    private const val SEPARATOR = "/"

    private fun pathUnder(parent: Tree?) = parent?.descendantPrefix ?: SEPARATOR
  }
}
