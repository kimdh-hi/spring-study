package com.toy.springwebfluxgraphql.lec08.config

import com.toy.springwebfluxgraphql.lec08.service.CustomerOrderDataFetcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer

@Configuration
class DataFetcherConfig(
  private val customerOrderDataFetcher: CustomerOrderDataFetcher
) {

  @Bean
  fun configurer() = RuntimeWiringConfigurer { configurer ->
    configurer.type("Query") {
      it.dataFetcher("customers", customerOrderDataFetcher)
    }
  }
}