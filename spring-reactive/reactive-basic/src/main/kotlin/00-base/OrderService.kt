package `00-base`

import reactor.core.publisher.Flux

class OrderService {

  companion object {
    val db = HashMap<Long, MutableList<PurchaseOrder>>()
  }

  init {
    val orderList1 = mutableListOf(
      PurchaseOrder.newOrder(1),
      PurchaseOrder.newOrder(1),
      PurchaseOrder.newOrder(1),
    )

    val orderList2 = mutableListOf(
      PurchaseOrder.newOrder(2),
      PurchaseOrder.newOrder(2),
      PurchaseOrder.newOrder(2),
    )
    db[1] = orderList1
    db[2] = orderList2
  }

  fun getOrders(userId: Long): Flux<PurchaseOrder> = Flux.create { sink ->
    db[userId]?.forEach {
      sink.next(it)
    }
    sink.complete()
  }
}