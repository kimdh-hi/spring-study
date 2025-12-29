package com.study.jpacore.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import org.hibernate.annotations.UuidGenerator
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited

@Entity
@Audited
class User private constructor(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var id: String? = null,

  @Column(length = 100, nullable = false)
  var name: String,
) : CreatedTimeAuditBaseEntity() {

  @OneToMany(mappedBy = "fromUser", cascade = [CascadeType.ALL], orphanRemoval = true)
  @NotAudited
  val fromUserFriends: MutableList<Friend> = mutableListOf()

  @OneToMany(mappedBy = "toUser", cascade = [CascadeType.ALL], orphanRemoval = true)
  @NotAudited
  val toUserFriends: MutableList<Friend> = mutableListOf()

  @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
  @NotAudited
  val datas: MutableList<UserData> = mutableListOf()

  companion object {
    fun of(name: String) = User(name = name)
  }

  fun updateName(name: String) {
    this.name = name
  }
}
