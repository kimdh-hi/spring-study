package com.toy.springredisevent.user.domain

import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.repository.CrudRepository

@Table(name = "tb_user")
@Entity
class User(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var name: String,

  @Embedded
  var status: UserStatus? = null
) {
  fun updateStatus(userStatus: UserStatus) {
    this.status = userStatus
  }

  fun initStatus() {
    this.status = null
  }
}

interface UserRepository: CrudRepository<User, String>