package com.toy.jpacustomgenerator.domain

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tb_user")
@EntityListeners(AuditingEntityListener::class)
class User(
  @Id
  @GeneratedValue(generator = "id")
  @GenericGenerator(name = "id",
    strategy = "com.toy.jpacustomgenerator.generator.CustomIdGenerator")
  @Column(name = "id")
  var id: String = "",

  var name: String,

  @CreatedDate
  var createdDate: LocalDateTime? = null
) {
  override fun toString(): String {
    return "User(id='$id', name='$name', createdDate=$createdDate)"
  }
}