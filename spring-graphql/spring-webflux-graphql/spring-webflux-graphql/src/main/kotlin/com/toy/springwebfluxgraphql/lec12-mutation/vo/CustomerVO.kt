package com.toy.springwebfluxgraphql.`lec12-mutation`.vo

import com.toy.springwebfluxgraphql.`lec12-mutation`.entity.Customer

data class CustomerVO(
  val id: Int? = null,
  val name: String,
  val age: Int,
  val city: String
) {

  companion object {
    fun of(customer: Customer) = CustomerVO(
      id = customer.id,
      name = customer.name,
      age = customer.age,
      city = customer.city
    )
  }

  fun toEntity() = Customer(
    id = id, name = name, age = age, city = city
  )
}