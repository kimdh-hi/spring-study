package com.toy.redissondistributedlock.domain

import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.GenericGenerator
import java.lang.Math.max
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Table
@Entity(name = "tb_space_channel")
class SpaceChannel(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

//  @ManyToOne(optional = false)
//  @JoinColumn(name = "space_id")
//  val space: Space,

  @ColumnDefault("0")
  var participants: Int = 0
) {
  fun participate() {
    this.participants++
  }

  fun exit() {
    this.participants = max(participants-1, 0)
  }
}