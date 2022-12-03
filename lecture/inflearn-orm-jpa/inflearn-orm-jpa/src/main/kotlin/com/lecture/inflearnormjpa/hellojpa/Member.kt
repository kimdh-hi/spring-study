package com.lecture.inflearnormjpa.hellojpa

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Member(
  @Id
  var id:Long? = null,

  var name: String
) {
  override fun toString(): String {
    return "Member(id=$id, name='$name')"
  }
}