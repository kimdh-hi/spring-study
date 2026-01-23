package com.study.springboot4.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull

inline fun <reified T, ID : Any> CrudRepository<T, ID>.get(id: ID): T {
  return findByIdOrNull(id) ?: throw RuntimeException("${T::class.simpleName} data not found. id=$id")
}
