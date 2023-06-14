package com.toy.springjpatsid.domain

import com.toy.springjpatsid.config.TSID_STRATEGY
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Table
@Entity(name = "tb_space")
@EntityListeners(AuditingEntityListener::class)
class Space(
  @Id
  @GeneratedValue(generator = "tsid")
  @GenericGenerator(name = "tsid", strategy = TSID_STRATEGY)
  @Column(length = 15)
  var id: String? = null,

  var name: String
) {

  @CreatedDate
  var createdDate: LocalDateTime? = null

  override fun toString(): String {
    return "Space(id=$id, name='$name', createdDate=$createdDate)"
  }
}