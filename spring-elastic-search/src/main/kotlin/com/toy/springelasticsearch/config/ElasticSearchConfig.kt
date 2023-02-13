package com.toy.springelasticsearch.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

//AbstractElasticsearchConfiguration deprecated
@Configuration
@EnableElasticsearchRepositories
class ElasticSearchConfig : ElasticsearchConfiguration() {
  override fun clientConfiguration(): ClientConfiguration = ClientConfiguration.builder()
      .connectedTo("localhost:9200")
      .build()
}