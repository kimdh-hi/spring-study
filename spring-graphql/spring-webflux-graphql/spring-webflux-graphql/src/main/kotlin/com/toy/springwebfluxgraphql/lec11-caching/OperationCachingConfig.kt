package com.toy.springwebfluxgraphql.`lec11-caching`

import graphql.execution.preparsed.PreparsedDocumentEntry
import graphql.execution.preparsed.PreparsedDocumentProvider
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ConcurrentHashMap

@Configuration
class OperationCachingConfig {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun sourceBuilderCustomizer(provider: PreparsedDocumentProvider) = GraphQlSourceBuilderCustomizer { customizer ->
    customizer.configureGraphQl { it.preparsedDocumentProvider(provider) }
  }

  @Bean
  fun provider(): PreparsedDocumentProvider {
    val map = ConcurrentHashMap<String, PreparsedDocumentEntry>()
    return PreparsedDocumentProvider { executionInput, parseAndValidateFunction ->
      map.computeIfAbsent(executionInput.query) {
        log.debug("PreparsedDocumentProvider key not found key: {}", it)
        val preparsedDocumentEntry = parseAndValidateFunction.apply(executionInput)
        log.debug("PreparsedDocumentProvider caching key: {}", preparsedDocumentEntry)
        preparsedDocumentEntry
      }
    }
  }
}