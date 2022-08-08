package com.toy.proxyex.app.v2

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RequestMapping
@ResponseBody
class OrderControllerV2(
  private val orderService: OrderServiceV2
) {

  @GetMapping("/v2/request")
  fun request(@RequestParam("itemId") itemId: String): String {
    orderService.orderItem(itemId)
    return "ok"
  }

  @GetMapping("/v2/no-log")
  fun noLog(): String = "ok"
}