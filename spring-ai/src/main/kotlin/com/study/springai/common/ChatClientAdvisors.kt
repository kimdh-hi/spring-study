package com.study.springai.common

import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClientRequest
import org.springframework.ai.chat.client.ChatClientResponse
import org.springframework.ai.chat.client.advisor.api.CallAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain
import org.springframework.core.Ordered

class SimpleLoggerAdvisor : CallAdvisor {

  private val logger = LoggerFactory.getLogger(this.javaClass)

  override fun adviseCall(
    chatClientRequest: ChatClientRequest,
    callAdvisorChain: CallAdvisorChain
  ): ChatClientResponse {
    logger.info("SimpleLoggerAdvisor.adviseCall request={}", chatClientRequest.prompt)
    val response = callAdvisorChain.nextCall(chatClientRequest)
    logger.info("SimpleLoggerAdvisor.adviseCall response={}", response.chatResponse()?.result?.output?.text)

    return response
  }

  override fun getName(): String = this.javaClass::getSimpleName.name

  override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE
}
