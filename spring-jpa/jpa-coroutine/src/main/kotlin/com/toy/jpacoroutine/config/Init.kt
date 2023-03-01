package com.toy.jpacoroutine.config

import com.toy.jpacoroutine.domain.Entity1
import com.toy.jpacoroutine.repository.Entity1Repository
import com.toy.jpacoroutine.repository.Entity2Repository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Init {

  @Bean
  fun commandLineRunner(
    entity1Repository: Entity1Repository,
    entity2Repository: Entity2Repository,
  ) = CommandLineRunner {

    val entity1s = mutableListOf<Entity1>()
    (1..100_000).map {
      entity1s.add(Entity1("entity1-id$it", "entity1-data$it"))
    }

    val entity2s = mutableListOf<Entity1>()
    (1..100_000).map {
      entity2s.add(Entity1("entity2-id$it", "entity2-data$it"))
    }

    entity1Repository.saveAll(entity1s)
    entity1Repository.saveAll(entity2s)
  }
}