package com.toy.jpabasic.domain

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.repository.CrudRepository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "tb_locker_user")
class LockerUser(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var username: String,

  @OneToOne(optional = false)
  @JoinColumn(name = "locker_id")
  var locker: Locker
) {
  override fun toString(): String {
    return "LockerUser(id=$id, username='$username', locker.id=${locker.id})"
  }
}

interface LockerUserRepository: CrudRepository<LockerUser, String>