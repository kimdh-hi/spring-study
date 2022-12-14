package com.lecture.inflearnormjpa.hellojpa

import java.time.LocalDateTime
import java.util.Date
import javax.persistence.*

@Entity
@Table(
  uniqueConstraints = [UniqueConstraint(columnNames = ["username"])]
)
class Member(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id:Long? = null,

  /*
  alter table Member
   add constraint UK_73pcydbur7ap0v0abheab5sfr unique (username)
   */

  @Column(nullable = false)
  var username: String,

  var name: String,

  var age: Int,

  @Enumerated(EnumType.STRING)
  var roleType: RoleType,

  @Temporal(TemporalType.TIMESTAMP)
  var createdDate: Date,

  @Temporal(TemporalType.TIMESTAMP)
  var lastModifiedDate: Date,

  @Lob
  var description: String,

  @Transient
  var temp: String

) {
  override fun toString(): String {
    return "Member(id=$id, name='$name')"
  }
}

enum class RoleType {
  USER, ADMIN
}