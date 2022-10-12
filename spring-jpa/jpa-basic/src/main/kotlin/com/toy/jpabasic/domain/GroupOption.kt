package com.toy.jpabasic.domain

import org.hibernate.annotations.ColumnDefault
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "tb_group_option")
class GroupOption(
  @Id
  @Column(name = "group_id")
  var groupId: String? = null,

  @OneToOne(optional = false)
  @JoinColumn(name = "group_id", referencedColumnName = "id", insertable = false, updatable = false)
  val group: Group,

  @ColumnDefault("0")
  @Column(name = "use_option")
  val useOption: Boolean
) {

  companion object {
    fun of(group: Group, useOption: Boolean): GroupOption {
      return GroupOption(
        groupId = group.id,
        group = group,
        useOption = useOption
      )
    }
  }

  override fun toString(): String {
    return "GroupOption(groupId=$groupId, useOption=$useOption)"
  }
}