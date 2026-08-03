package com.study.concurrency.stock.ui

import com.study.concurrency.stock.application.DecreaseStrategy
import com.study.concurrency.stock.application.StockDecreaseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class CreateStockRequest(
  val productId: Long,
  val quantity: Long,
)

data class DecreaseStockRequest(
  val quantity: Long,
)

data class StockResponse(
  val id: Long,
  val quantity: Long,
)

@RestController
@RequestMapping("/api/stocks/{strategy}")
class StockController(
  services: List<StockDecreaseService>,
) {
  private val servicesByStrategy = services.associateBy { it.strategy }

  @PostMapping
  fun create(
    @PathVariable strategy: DecreaseStrategy,
    @RequestBody request: CreateStockRequest,
  ): StockResponse {
    val service = serviceOf(strategy)
    val id = service.create(request.productId, request.quantity)
    return StockResponse(id, service.quantityOf(id))
  }

  @PostMapping("/{id}/decrease")
  fun decrease(
    @PathVariable strategy: DecreaseStrategy,
    @PathVariable id: Long,
    @RequestBody request: DecreaseStockRequest,
  ): StockResponse {
    val service = serviceOf(strategy)
    service.decrease(id, request.quantity)
    return StockResponse(id, service.quantityOf(id))
  }

  @GetMapping("/{id}")
  fun get(
    @PathVariable strategy: DecreaseStrategy,
    @PathVariable id: Long,
  ): StockResponse = StockResponse(id, serviceOf(strategy).quantityOf(id))

  private fun serviceOf(strategy: DecreaseStrategy): StockDecreaseService =
    servicesByStrategy[strategy] ?: throw IllegalArgumentException("unsupported strategy. strategy=$strategy")
}
