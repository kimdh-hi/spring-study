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
@Table(name = "tb_user2")
@EntityListeners(AuditingEntityListener::class)
class User2(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String = "",

  var name: String,

  @CreatedDate
  var createdDate: LocalDateTime? = null
) {
  override fun toString(): String {
    return "User2(id='$id', name='$name', createdDate=$createdDate)"
  }
}