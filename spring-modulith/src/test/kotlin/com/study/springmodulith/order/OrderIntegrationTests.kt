package com.study.springmodulith.order

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.modulith.test.AssertablePublishedEvents

@ApplicationModuleTest
class OrderIntegrationTests(
  private val orderService: OrderService,
) {

  @Test
  fun publishesOrderCompletedEvent(events: AssertablePublishedEvents) {
    orderService.placeOrder("keyboard")

    assertThat(events)
      .contains(OrderCompletedEvent::class.java)
      .matching { it.product == "keyboard" }
  }
}
