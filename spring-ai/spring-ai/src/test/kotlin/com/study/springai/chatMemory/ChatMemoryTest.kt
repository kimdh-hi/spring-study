package com.study.springai.chatMemory

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.ai.vectorstore.pgvector.PgVectorStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.Ordered

/**
 * LLM 은 기본적으로 상태를 저장하지 않는다.
 * 대화 세션에서 맥락을 유지하기 위해 userMessage, assistantMessage 를 저장하고, LLM 질의시 맥락 유지를 위해 저장된 메세지를 사용한다.
 *
 * ChatMemoryRepository
 * - InMemoryChatMemoryRepository (default)
 * - JdbcChatMemoryRepository
 * - CassandraChatMemoryRepository
 *
 * ChatMemory
 * - MessageWindowChatMemory (default) - sliding window(window default: 20) 기반 메세지 저장
 *
 * ChatMemoryAutoConfiguration
 *
 * MessageChatMemoryAdvisor
 * - chatMemory로부터 이전 message(userMessage, assistantMessage) 를 받아 prompt 에 포함
 *
 * PromptChatMemoryAdvisor
 * - chatMemory로부터 받은 이전 message 를 받아 systemMessage 에 포함
 *
 * VectorStoreChatMemoryAdvisor
 * JdbcChatMemoryRepository
 * CassandraChatMemoryRepository
 */

@SpringBootTest
class ChatMemoryTest @Autowired constructor(
  private val chatClientBuilder: ChatClient.Builder,
  private val vectorStore: PgVectorStore,
) {

  @Test
  fun vectorStoreMemory() {
    val chatClient = chatClientBuilder.defaultAdvisors(
      VectorStoreChatMemoryAdvisor.builder(vectorStore).build(),
      SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
    )
      .build()

    chatClient.prompt()
      .user("내 이름은 김대현이야")
      .call()
      .content()!!

    chatClient.prompt()
      .user("내 이름이 뭐라고?")
      .call()
      .content()!!
  }

  @Test
  fun inMemory() {
    val chatMemory = MessageWindowChatMemory.builder().maxMessages(2).build()

    val chatClient = chatClientBuilder.defaultAdvisors(
//      MessageChatMemoryAdvisor.builder(chatMemory).build(),
      PromptChatMemoryAdvisor.builder(chatMemory).build(),
      SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
    )
      .build()

    chatClient.prompt()
      .user("내 이름은 김대현이야")
      .call()
      .content()!!

    chatClient.prompt()
      .user("내 이름이 뭐라고?")
      .call()
      .content()!!

    chatClient.prompt()
      .user("다시 한 번 내 이름이 뭐라고?")
      .call()
      .content()!!
  }
}
