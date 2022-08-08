package com.toy.proxyex.app.v1

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@RequestMapping
@ResponseBody
interface OrderControllerV1 {

  @GetMapping("/v1/request")
  fun request(@RequestParam("itemId") itemId: String): String

  @GetMapping("/v1/no-log")
  fun noLog(): String
}

class OrderControllerV1Impl(
  private val orderService: OrderServiceV1
): OrderControllerV1 {
  override fun request(itemId: String): String {
    orderService.orderItem(itemId)
    return "ok"
  }

  override fun noLog(): String = "ok"
}

