package com.toy.noticeservice.domain

import com.toy.noticeservice.domain.enum.Authority
import java.io.Serial
import javax.persistence.Table

@Table(name = "tb_role")
class Role (
  var id: String,
  var name: String,
  var authorities: Set<Authority>
): AbstractBaseEntity() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 442759895868076602L
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Role

    if (name != other.name) return false

    return true
  }

  override fun hashCode(): Int {
    return name.hashCode()
  }

  override fun toString(): String {
    return "Role(id='$id', name='$name', authorities=$authorities)"
  }

}