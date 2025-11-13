package com.study.springai.embedding

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.ai.document.Document
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.pgvector.PgVectorStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TextEmbeddingTest @Autowired constructor(
  private val embeddingModel: EmbeddingModel,
  private val vectorStore: PgVectorStore,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Test
  fun search() {
    val searchRequest = SearchRequest.builder()
      .query("대통령은 얼마나 근무해?")
      .topK(4)
      .filterExpression("source == '헌법' && year >= 1987")
      .similarityThreshold(0.4)
      .build()

    val result = vectorStore.similaritySearch(searchRequest)

    result.forEach {
      log.info("result={}", it.text)
    }
  }

  @Test
  fun addDocument() {
    val documents = listOf(
      Document("대통령 선거는 5년마다 있습니다.", mapOf("source" to "헌법", "year" to 1987)),
      Document("대통령 임기는 4년입니다.", mapOf("source" to "헌법", "year" to 1980)),
      Document("국회의원은 법률안을 심의, 의결합니다.", mapOf("source" to "헌법", "year" to 1987)),
      Document("자동차를 사용하려면 등록을 해야합니다.", mapOf("source" to "자동차관리법")),
      Document("대통령은 행정부의 수반입니다.", mapOf("source" to "헌법", "year" to 1987)),
      Document("국회의원은 4년마다 투표로 뽑습니다.", mapOf("source" to "헌법", "year" to 1987)),
      Document("승용차는 정규적인 점검이 필요합니다.", mapOf("source" to "자동차관리법")),
    )

    vectorStore.add(documents)
  }

  @Test
  fun test() {
    val embeddingResponse = embeddingModel.embedForResponse(listOf("test"))
    log.info("embeddingResponse.metadata.model={}", embeddingResponse.metadata.model)
    log.info("embeddingModel.dimensions={}", embeddingModel.dimensions())

    embeddingResponse.results.firstOrNull()?.let {
      log.info("vector dimension={}", it.output.size)
      log.info("vector={}", it.output)
    }
  }
}
