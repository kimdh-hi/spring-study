package com.toy.springwebfluxgraphql.lec07.domain

data class CustomerWithOrder(
  val id: Long,
  val name: String,
  val age: Int,
  val address: String,
  var orders: List<CustomerOrder>
) {
  companion object {
    fun of(customer: Customer, orders: List<CustomerOrder> = listOf()) = CustomerWithOrder (
      id = customer.id,
      name = customer.name,
      age = customer.age,
      address = customer.address,
      orders = orders
    )
  }
}