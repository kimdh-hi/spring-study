package com.toy.noticeservice.domain

import java.io.Serial
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "tb_notice")
class Notice (
  @Id
  var id: String? = null,

  var title: String,
  var content: String,

  var userId: String,
): AbstractTraceByEntity() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 154605313969232868L
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Notice

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }

  override fun toString(): String {
    return "Notice(id='$id', title='$title', content='$content', userId='$userId')"
  }

}