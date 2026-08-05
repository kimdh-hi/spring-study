package com.study.sqsproducer.ui

import com.study.sqsproducer.application.OrderService
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(
  private val orderService: OrderService,
) {
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun place(@RequestBody @Valid request: PlaceOrderRequest): OrderResponse =
    OrderResponse(orderService.place(request.productName, request.quantity, request.customerId).id)

  @PostMapping("/fifo")
  @ResponseStatus(HttpStatus.CREATED)
  fun placeOrdered(@RequestBody @Valid request: PlaceOrderRequest): OrderResponse =
    OrderResponse(orderService.placeOrdered(request.productName, request.quantity, request.customerId).id)

  data class PlaceOrderRequest(
    @field:NotBlank val productName: String,
    @field:Min(1) val quantity: Int,
    @field:NotBlank val customerId: String,
  )

  data class OrderResponse(val orderId: Long)
}
