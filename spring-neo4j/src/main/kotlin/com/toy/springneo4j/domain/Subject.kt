package com.toy.springneo4j.domain

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node

@Node(labels = ["Subject"])
class Subject(
  @Id
  @GeneratedValue
  var id: Long? = null,

  var name: String
)