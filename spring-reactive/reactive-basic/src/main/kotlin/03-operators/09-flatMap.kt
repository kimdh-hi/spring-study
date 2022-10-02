package `03-operators`

import `00-base`.OrderService
import `00-base`.UserService

fun main() {
  val orderService = OrderService()
  UserService().getUsers()
    .flatMap { orderService.getOrders(it.userId) }
    .subscribe { println(it) }
}
