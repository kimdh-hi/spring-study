package com.study.springelasticsearch

import com.study.springelasticsearch.product.application.ProductService
import com.study.springelasticsearch.product.domain.repository.ProductRepository
import com.study.springelasticsearch.product.infra.ProductDocument
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.elasticsearch.ElasticsearchContainer
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@ActiveProfiles("test")
@Import(ProductSearchTest.ElasticsearchTestConfig::class)
class ProductSearchTest {

  @TestConfiguration(proxyBeanMethods = false)
  class ElasticsearchTestConfig {
    @Bean
    @ServiceConnection
    fun elasticsearchContainer(): ElasticsearchContainer =
      ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:9.4.2")
        .withEnv("xpack.security.enabled", "false")
        .withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m")
  }

  @Autowired
  private lateinit var productService: ProductService

  @Autowired
  private lateinit var operations: ElasticsearchOperations

  @Autowired
  private lateinit var productRepository: ProductRepository

  @BeforeEach
  fun setUp() {
    productRepository.deleteAll()

    val indexOperations = operations.indexOps(ProductDocument::class.java)
    indexOperations.delete()
    indexOperations.createWithMapping()

    productService.register("무선 마우스", "저소음 블루투스 무선 마우스", "주변기기", 29000)
    productService.register("게이밍 마우스", "초경량 유선 게이밍 마우스", "주변기기", 59000)
    productService.register("노트북 스탠드", "알루미늄 노트북 거치대. 무선 충전 지원", "액세서리", 39000)

    indexOperations.refresh()
  }

  @Test
  fun `name 필드 가중치로 관련도 순 정렬되고 하이라이트가 붙는다`() {
    val results = productService.search("마우스")

    assertEquals(2, results.size)
    assertTrue(results[0].score >= results[1].score)
    assertTrue(results.all { it.highlights.any { highlight -> highlight.contains("<em>마우스</em>") } })
  }

  @Test
  fun `여러 필드를 한 번에 검색한다 - name 에만 있는 문서와 description 에만 있는 문서가 함께 잡힌다`() {
    val results = productService.search("무선").map { it.name }

    assertContains(results, "무선 마우스")
    assertContains(results, "노트북 스탠드")
  }

  @Test
  fun `검색 결과를 카테고리로 집계한다`() {
    val facet = productService.categoryFacet("무선")

    assertEquals(mapOf("주변기기" to 1L, "액세서리" to 1L), facet)
  }

  @Test
  fun `JPA LIKE 검색은 형태소가 아닌 문자열 포함 여부만 본다`() {
    assertEquals(2, productService.searchByLike("마우스").size)
    assertEquals(0, productService.searchByLike("마우스 무선").size)
    assertEquals(3, productService.search("마우스 무선").size)
  }
}
