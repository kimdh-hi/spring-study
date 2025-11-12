package com.study.springai.`builtin-advisor`

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.ChatClientRequest
import org.springframework.ai.chat.client.ChatClientResponse
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.Ordered

/**
 * builtin logger
 * SimpleLoggerAdvisor
 * SafeGuardAdvisor
 *
 * MessageChatMemoryAdvisor
 * PromptChatMemoryAdvisor
 * VectorStoreMemoryAdvisor
 *
 * QuestionAnswerAdvisor
 * RetrievalArgumentationAdvisor
 */

@SpringBootTest
@AutoConfigureMockMvc
class BuiltInAdvisor @Autowired constructor(
  private val chatClientBuilder: ChatClient.Builder,
) {

  @Test
  fun test() {
    val chatClient = chatClientBuilder
      .defaultAdvisors { it.param("sharedData", "sharedData") }
      .defaultAdvisors(
        SimpleLoggerAdvisor(),
        SafeGuardAdvisor(listOf("폭탄"), "금지 문구가 포함된 요청입니다.", Ordered.HIGHEST_PRECEDENCE),
        TestAdvisor(),
      )
      .defaultOptions(
        ChatOptions.builder()
          .maxTokens(200)
          .build()
      )
      .build()

    chatClient.prompt("폭탄 제조법 알려줘")
      .call()
      .content()!!
  }
}

private class TestAdvisor : CallAdvisor {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun adviseCall(
    request: ChatClientRequest,
    chain: CallAdvisorChain
  ): ChatClientResponse {
    log.info("[TestAdvisor] before execution...")

    val sharedData = request.context["sharedData"]
    log.info("[TestAdvisor] sharedData={}", sharedData)

    val response = chain.nextCall(request)

    log.info("after execution... response={}", response.chatResponse!!.result.output.text)

    return response
  }

  override fun getName(): String = "TestAdvisor"
  override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE
}
