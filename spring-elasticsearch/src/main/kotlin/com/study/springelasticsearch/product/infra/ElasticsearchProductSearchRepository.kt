package com.study.springelasticsearch.product.infra

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation
import co.elastic.clients.elasticsearch._types.query_dsl.Query
import com.study.springelasticsearch.product.domain.model.Product
import com.study.springelasticsearch.product.domain.model.ProductSearchResult
import com.study.springelasticsearch.product.domain.repository.ProductSearchRepository
import jakarta.annotation.PostConstruct
import org.springframework.data.domain.PageRequest
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations
import org.springframework.data.elasticsearch.client.elc.NativeQuery
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.query.HighlightQuery
import org.springframework.data.elasticsearch.core.query.highlight.Highlight
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField
import org.springframework.stereotype.Repository

@Repository
class ElasticsearchProductSearchRepository(
  private val operations: ElasticsearchOperations,
) : ProductSearchRepository {

  private val indexOperations = operations.indexOps(ProductDocument::class.java)

  @PostConstruct
  fun createIndex() {
    if (!indexOperations.exists()) {
      indexOperations.createWithMapping()
    }
  }

  override fun index(product: Product) {
    operations.save(ProductDocument.from(product))
  }

  override fun search(keyword: String, size: Int): List<ProductSearchResult> {
    val query = NativeQuery.builder()
      .withQuery(matchQuery(keyword))
      .withHighlightQuery(
        HighlightQuery(
          Highlight(listOf(HighlightField("name"), HighlightField("description"))),
          ProductDocument::class.java,
        ),
      )
      .withPageable(PageRequest.ofSize(size))
      .build()

    return operations.search(query, ProductDocument::class.java)
      .map { hit ->
        ProductSearchResult(
          id = hit.content.id.toLong(),
          name = hit.content.name,
          category = hit.content.category,
          price = hit.content.price,
          score = hit.score,
          highlights = hit.highlightFields.values.flatten(),
        )
      }
      .toList()
  }

  override fun countByCategory(keyword: String): Map<String, Long> {
    val query = NativeQuery.builder()
      .withQuery(matchQuery(keyword))
      .withAggregation("byCategory", Aggregation.of { it.terms { terms -> terms.field("category") } })
      .withMaxResults(0)
      .build()

    val aggregations = operations.search(query, ProductDocument::class.java)
      .aggregations as ElasticsearchAggregations

    return aggregations.aggregationsAsMap()["byCategory"]!!
      .aggregation().aggregate.sterms().buckets().array()
      .associate { it.key().stringValue() to it.docCount() }
  }

  private fun matchQuery(keyword: String): Query = Query.of { query ->
    query.multiMatch { it.fields("name^3", "description").query(keyword) }
  }
}
