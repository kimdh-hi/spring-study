package com.toy.springdataenvers.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.UuidGenerator
import org.hibernate.envers.Audited
import java.time.LocalDateTime

@Entity
@Table(name = "tb_soft_delete")
@SQLDelete(sql = "update deleted_at set deleted_at=now() where id=?")
@SQLDelete(sql = "update deleted set deleted=0 where id=?")
@Audited
class SoftDeleteEntity(
  @Id
  @UuidGenerator
  var id: String = "",

  var data: String,

  @Column(name = "deleted_at")
  var deletedAt: LocalDateTime? = null,

  var deleted: Boolean = false
)