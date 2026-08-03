package com.study.springgraalvm.domain.model

import com.study.springgraalvm.common.BaseTimeEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Company(
  var name: String,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,
) : BaseTimeEntity() {

  @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], orphanRemoval = true)
  val users: MutableList<User> = mutableListOf()

  fun addUser(user: User) {
    users.add(user)
  }
}
