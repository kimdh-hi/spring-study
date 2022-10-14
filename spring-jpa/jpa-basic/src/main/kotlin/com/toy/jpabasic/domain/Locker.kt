package com.toy.jpabasic.domain

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.repository.CrudRepository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "tb_locker")
class Locker(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var number: Int,

) {
  override fun toString(): String {
    return "Locker(id=$id, number=$number)"
  }
}


interface LockerRepository: CrudRepository<Locker, String>