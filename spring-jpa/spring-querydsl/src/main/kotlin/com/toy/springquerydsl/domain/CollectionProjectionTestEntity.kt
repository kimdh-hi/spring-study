package com.toy.springquerydsl.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table


@Entity(name = "tb_collection_projection_parent")
@Table
class CollectionProjectionParent(

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  val data1: String
) {

  @OneToMany(mappedBy = "collectionProjectionParent", cascade = [CascadeType.ALL], orphanRemoval = true)
  var collectionProjectionChildren: MutableList<CollectionProjectionChildren> = mutableListOf()
}

@Entity(name = "tb_collection_projection_children")
@Table
class CollectionProjectionChildren(

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  val data2: String,

  @ManyToOne(optional = false)
  @JoinColumn(name = "collection_projection_parent_id")
  val collectionProjectionParent: CollectionProjectionParent
)

