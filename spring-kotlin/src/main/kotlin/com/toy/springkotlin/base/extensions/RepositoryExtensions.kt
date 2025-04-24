package com.toy.springkotlin.base.extensions

import org.springframework.data.repository.CrudRepository

//fun <T, ID> CrudRepository<T, ID>.findByIdOrThrow(id: ID): T? =
//  findById(id!!).orElse(null) ?: throw RuntimeException("data not found. id=$id")

inline fun <reified T, ID> CrudRepository<T, ID>.findByIdOrThrow(id: ID): T? =
  findById(id!!).orElse(null) ?: throw RuntimeException("[Entity: ${T::class.simpleName}] data not found. id=$id")
