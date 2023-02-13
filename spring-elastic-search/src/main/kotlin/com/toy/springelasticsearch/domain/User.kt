package com.toy.springelasticsearch.domain

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "users")
@Entity
@Table(name = "tb_user")
class User(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  @Column(nullable = false)
  var name: String,

  var description: String
)