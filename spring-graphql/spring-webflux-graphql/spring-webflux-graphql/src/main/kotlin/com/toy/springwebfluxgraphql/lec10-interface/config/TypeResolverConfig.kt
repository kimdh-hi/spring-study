package com.toy.springwebfluxgraphql.`lec10-interface`.config

import com.toy.springwebfluxgraphql.`lec10-interface`.dto.FruitDto
import graphql.schema.TypeResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.ClassNameTypeResolver
import org.springframework.graphql.execution.RuntimeWiringConfigurer

/**
 * graphql 은 기본적으로 schema 파일의 타입명과 class 명을 기반으로 매핑
 * type Fruit implements Product {...} --> data class Fruit
 *
 * vo, dto 등 schema 파일과는 다른 이름으로 매핑하고자 한다면 TypeResolver 를 설정
 * * type Fruit implements Product {...} --> data class FruitDto
 */
@Configuration
class TypeResolverConfig {

  @Bean
  fun configurer(typeResolver: TypeResolver) = RuntimeWiringConfigurer { configurer ->
    configurer.type("Product") { it.typeResolver(typeResolver) }
  }

  @Bean
  fun typeResolver(): TypeResolver {
    return ClassNameTypeResolver().apply {
      addMapping(FruitDto::class.java, "Fruit")
    }
  }
}