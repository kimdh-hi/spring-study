package com.toy.jpacoroutine.config

import com.toy.jpacoroutine.domain.Entity1
import com.toy.jpacoroutine.domain.Entity2
import com.toy.jpacoroutine.repository.Entity1Repository
import com.toy.jpacoroutine.repository.Entity2Repository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class Init(
  private val entity1Repository: Entity1Repository,
  private val entity2Repository: Entity2Repository
): ApplicationRunner {

  override fun run(args: ApplicationArguments) {
    val entities1 = mutableListOf<Entity1>()
    (1..1000).map {
      entities1.add(Entity1(data = "data$it"))
    }

    val entities2 = mutableListOf<Entity2>()
    (1..1000).map {
      entities2.add(Entity2(data = "data$it"))
    }

    entities1.add(Entity1(data = "~~target~~"))
    entities2.add(Entity2(data = "~~target~~"))

    entity1Repository.saveAll(entities1)
    entity2Repository.saveAll(entities2)
  }
}