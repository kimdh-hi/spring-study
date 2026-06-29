package com.study.springai.rag

import org.junit.jupiter.api.Test
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.pgvector.PgVectorStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.Ordered

/**
 * RAG (검색 증상 생성, Retrieval-Augmented Generation)
 * 질문의 해답을 위해 vectorStore 에서 우선 검색 후 질문에 문맥을 추가하여 프롬프트를 증간한다.
 *
 * QuestionAnswerAdvisor
 * - prompt 내용 증강
 * - 사용자의 질문으로 vectorStore 를 검색 후 검색된 결과를 prompt 에 포함시킨다.
 *
 * RetrievalAugmentationAdvisor
 */

@SpringBootTest
class RagTest @Autowired constructor(
  private val chatClientBuilder: ChatClient.Builder,
  private val vectorStore: PgVectorStore,
) {

  @Test
  fun questionAnswerAdvisor() {
    val searchRequest = SearchRequest.builder()
      .similarityThreshold(0.5)
      .build()

    val questionAnswerAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
      .searchRequest(searchRequest)
      .build()

    val chatClient = chatClientBuilder
      .defaultAdvisors(
        questionAnswerAdvisor,
        SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1),
      )
      .build()

    chatClient.prompt()
      .user("대통령의 임기는?")
      .call()
      .content()!!
  }
}
