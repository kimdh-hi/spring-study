package com.toy.springkotlin.base.extensions

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull

//fun <T, ID> CrudRepository<T, ID>.findByIdOrThrow(id: ID): T? =
//  findById(id!!).orElse(null) ?: throw RuntimeException("data not found. id=$id")

inline fun <reified T, ID> CrudRepository<T, ID>.findByIdOrThrow(id: ID): T? =
  findByIdOrNull(id) ?: throw RuntimeException("[Entity: ${T::class.simpleName}] data not found. id=$id")

// val entity = repository[id]
inline operator fun <reified T, ID> CrudRepository<T, ID>.get(id: ID): T? = findByIdOrThrow(id)
