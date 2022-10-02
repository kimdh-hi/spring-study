package com.toy.jpajooq.domain

import java.io.Serial
import java.io.Serializable
import javax.persistence.*

@Table
@Entity(name = "mt_group_user")
class GroupUser(

  @ManyToOne(optional = false)
  @JoinColumn(name = "group_id", nullable = false, insertable = false, updatable = false)
  final val group: Group,

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
  final val user: User,

  val key: String
) {
  @EmbeddedId
  var id: ID = ID(groupId = group.id!!, userId = user.id!!)

  @Embeddable
  data class ID (
    @Column(name = "group_id", length = 50)
    var groupId: String? = null,

    @Column(name = "user_id", length = 50)
    var userId: String? = null,
  ): Serializable {
    companion object {
      @Serial
      private const val serialVersionUID: Long = -91322684155083939L
    }
  }
}