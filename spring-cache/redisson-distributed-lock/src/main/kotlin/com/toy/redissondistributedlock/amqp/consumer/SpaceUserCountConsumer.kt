package com.toy.redissondistributedlock.amqp.consumer

import com.toy.redissondistributedlock.service.SpaceService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import java.io.Serial
import java.io.Serializable

@Component
class SpaceUserCountConsumer(
  private val spaceService: SpaceService,
) {

  @RabbitListener(queues = ["space.user-count"])
  fun consume(message: SpaceUserCountAdjustMessage) {
    if(message.isIncrease) {
      spaceService.increaseSpaceUserCount(message.spaceId)
    } else {
      // decrease
    }
  }
}

data class SpaceUserCountAdjustMessage(
  val spaceId: String,
  val isIncrease: Boolean
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -2627812485421671187L
  }
}