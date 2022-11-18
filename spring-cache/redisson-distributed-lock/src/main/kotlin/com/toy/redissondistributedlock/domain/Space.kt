package com.toy.redissondistributedlock.domain

import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import kotlin.math.max

@Table
@Entity(name = "tb_space")
class Space(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  val name: String,

  @ColumnDefault("3")
  @Column(name = "max_user_count")
  val maxUserCount: Int = 10,

  @ColumnDefault("0")
  @Column(name = "participants")
  var participants: Int = 0
) {
  fun participate() {
    if (participants + 1 <= maxUserCount)
    this.participants++
  }

  fun exit() {
    this.participants = max(participants - 1, 0)
  }
}