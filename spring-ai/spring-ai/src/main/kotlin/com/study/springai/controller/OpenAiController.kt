package com.study.springai.controller

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.ChatClientRequest
import org.springframework.ai.chat.client.ChatClientResponse
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.core.Ordered
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
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
  suspend fun streamingTest(@PathVariable message: String): Flow<String> {
    val chatClient = chatClientBuilder
      .defaultAdvisors(
        MessageChatMemoryAdvisor.builder(chatMemory).build(),
        SimpleLoggerAdvisor()
      )
      .build()

    return chatClient
      .prompt()
      .system("질문에 대해 한국어로 답변합니다")
      .user(message)
      .options(
        ChatOptions.builder()
          .temperature(0.3)
          .maxTokens(100)
          .build()
      )
      .stream()
      .content()
      .asFlow()
  }
}

private class SimpleLoggerAdvisor : CallAdvisor {

  private val logger = LoggerFactory.getLogger(this.javaClass)

  override fun adviseCall(
    chatClientRequest: ChatClientRequest,
    callAdvisorChain: CallAdvisorChain
  ): ChatClientResponse {
    logger.info("SimpleLoggerAdvisor.adviseCall request={}", chatClientRequest.prompt.contents)
    val response = callAdvisorChain.nextCall(chatClientRequest)
    logger.info("SimpleLoggerAdvisor.adviseCall response={}", response.chatResponse()?.result?.output?.text)

    return response
  }

  override fun getName(): String = this.javaClass::getSimpleName.name

  override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE
}
