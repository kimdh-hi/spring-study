package com.toy.springquerydsl.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table


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
  var collectionProjectionChildren: MutableList<CollectionProjectionChild> = mutableListOf()

  override fun toString(): String {
    return "CollectionProjectionParent(id=$id, data1='$data1', collectionProjectionChildren=$collectionProjectionChildren)"
  }
}

@Entity(name = "tb_collection_projection_child")
@Table
class CollectionProjectionChild(

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  val data2: String,

  @ManyToOne(optional = false)
  @JoinColumn(name = "collection_projection_parent_id")
  @JsonIgnore
  val collectionProjectionParent: CollectionProjectionParent
) {
  override fun toString(): String {
    return "CollectionProjectionChild(id=$id, data2='$data2')"
  }
}

