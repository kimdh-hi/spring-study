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
import org.springframework.data.elasticsearch.core.SearchHit
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
      .withQuery(multiMatch(keyword))
      .withHighlightQuery(highlightOn("name", "description"))
      .withPageable(PageRequest.ofSize(size))
      .build()

    return operations.search(query, ProductDocument::class.java)
      .map { it.toSearchResult() }
      .toList()
  }

  override fun countByCategory(keyword: String): Map<String, Long> {
    val query = NativeQuery.builder()
      .withQuery(multiMatch(keyword))
      .withAggregation(CATEGORY_AGGREGATION, termsOn("category"))
      .withMaxResults(0)
      .build()

    val aggregations = operations.search(query, ProductDocument::class.java)
      .aggregations as ElasticsearchAggregations
    val buckets = aggregations.aggregationsAsMap()
      .getValue(CATEGORY_AGGREGATION)
      .aggregation().aggregate
      .sterms().buckets().array()

    return buckets.associate { it.key().stringValue() to it.docCount() }
  }

  private fun multiMatch(keyword: String): Query = Query.of { query ->
    query.multiMatch { it.fields("name^3", "description").query(keyword) }
  }

  private fun highlightOn(vararg fields: String): HighlightQuery = HighlightQuery(
    Highlight(fields.map { HighlightField(it) }),
    ProductDocument::class.java,
  )

  private fun termsOn(field: String): Aggregation = Aggregation.of { aggregation ->
    aggregation.terms { it.field(field) }
  }

  private fun SearchHit<ProductDocument>.toSearchResult() = ProductSearchResult(
    id = content.id.toLong(),
    name = content.name,
    category = content.category,
    price = content.price,
    score = score,
    highlights = highlightFields.values.flatten(),
  )

  companion object {
    private const val CATEGORY_AGGREGATION = "byCategory"
  }
}
