package com.study.springai.controller

import com.study.springai.common.SimpleLoggerAdvisor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/openai")
class OpenAiController(
  private val chatClientBuilder: ChatClient.Builder,
  private val chatMemory: ChatMemory,
) {

  private val logger = LoggerFactory.getLogger(this.javaClass)

  @GetMapping("/{message}")
  fun test(@PathVariable message: String): ResponseEntity<String> {
    val chatClient = chatClientBuilder
      .defaultAdvisors(
        MessageChatMemoryAdvisor.builder(chatMemory).build(),
        SimpleLoggerAdvisor()
      )
      .build()

    val chatResponse = chatClient
      .prompt()
      .system("질문에 대해 한국어로 답변합니다")
      .user(message)
      .options(
        ChatOptions.builder()
          .temperature(0.3)
          .maxTokens(100)
          .build()
      )
      .call()
      .chatResponse()
    val metaData = chatResponse!!.metadata
    val text = chatResponse.result.output.text

    logger.info("metaData={}", metaData)
    logger.info("text={}", text)

    return ResponseEntity.ok(text)
  }

  @GetMapping("/streaming/{message}", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
  suspend fun streamingTest(@PathVariable message: String, @RequestParam language: String): Flow<String> {
    val chatClient = chatClientBuilder
      .defaultSystem("답변의 가장 앞에 [TEST] 문구를 붙여줘.")
      .defaultOptions(
        ChatOptions.builder()
          .maxTokens(100)
          .build()
      )
      .defaultAdvisors(
        MessageChatMemoryAdvisor.builder(chatMemory).build(),
        SimpleLoggerAdvisor()
      )
      .build()

    val systemPromptTemplate = SystemPromptTemplate.builder()
      .template("질문에 대해 {language} 언어로 답변합니다")
      .build()
    val systemPromptMessage = systemPromptTemplate.createMessage(mapOf("language" to language))
    return chatClient
      .prompt()
//      .messages(systemPromptMessage)
      .system(systemPromptTemplate.render(mapOf("language" to language)))
      .user(message)
      .options(
        ChatOptions.builder()
          .temperature(0.3)
          .build()
      )
      .stream()
      .content()
      .asFlow()
  }
}

