package com.toy.springneo4j.domain

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Property
import org.springframework.data.neo4j.core.schema.Relationship

@Node(labels = ["Student"])
class Student(
  @Id
  @GeneratedValue
  var id: Long? = null,

  var name: String,

  @Property(name = "birth_year")
  var birthYear: Int,

  @Relationship(type = "belongs_to", direction = Relationship.Direction.OUTGOING)
  val studentClass: StudentClass,

  @Relationship(type = "is_learning", direction = Relationship.Direction.OUTGOING)
  val subjects: MutableList<Subject>
)