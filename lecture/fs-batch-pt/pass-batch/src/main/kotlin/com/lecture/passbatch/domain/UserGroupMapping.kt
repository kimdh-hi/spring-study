package com.lecture.passbatch.domain

import java.io.Serial
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "user_group_mapping")
@IdClass(UserGroupMappingId::class)
class UserGroupMapping(
  @Id
  val userGroupId: String,

  @Id
  val userId: String,

  val userGroupName: String,
  var description: String
): BaseEntity()

class UserGroupMappingId(
  val userGroupId: String,
  val userId: String,
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 8309946477875386868L
  }
}