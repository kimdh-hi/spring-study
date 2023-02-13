package com.toy.springelasticsearch

import com.toy.springelasticsearch.repository.UserSearchRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(
  excludeFilters = [ComponentScan.Filter(
    type = FilterType.ASSIGNABLE_TYPE,
    classes = [UserSearchRepository::class]
  )]
)
@SpringBootApplication
class SpringElasticSearchApplication

fun main(args: Array<String>) {
  runApplication<SpringElasticSearchApplication>(*args)
}
