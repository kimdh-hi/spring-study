package com.toy.jpabasic.service

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable

@Transactional(readOnly = true)
abstract class GenericService<T : Serializable, ID : Serializable>(
  val repository: CrudRepository<T, ID>
) {

  fun get(id: ID): T {
    return repository.findByIdOrNull(id) ?: throw IllegalArgumentException("data not found ...")
  }

  fun getOrNull(id: ID): T? {
    return repository.findByIdOrNull(id)
  }

  fun exists(id: ID): Boolean {
    return repository.existsById(id)
  }

  @Transactional
  fun save(entity: T): T {
    return repository.save(entity)
  }

  @Transactional
  fun deleteById(id: ID) {
    repository.deleteById(id)
  }

  @Transactional
  fun delete(entity: T) {
    repository.delete(entity)
  }

}