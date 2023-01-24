package com.toy.springwebfluxgraphql.lec13.vo

import com.toy.springwebfluxgraphql.lec13.entity.Customer

data class CustomerVO(
  var id: Int? = null,
  var name: String,
  var age: Int,
  var city: String
) {

  companion object {
    fun of(customer: Customer) = CustomerVO(
      id = customer.id,
      name = customer.name,
      age = customer.age,
      city = customer.city
    )
  }

  fun toEntity(id: Int? = null) = Customer(
    id = id, name = name, age = age, city = city
  )
}

data class CustomerDeleteResponseVO(
  val id: Int,
  val status: CustomerDeleteStatus
)

enum class CustomerDeleteStatus {
  SUCCESS, FAILURE
}