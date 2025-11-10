package com.study.springai.controller

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/openai")
class OpenAiController(
  private val chatModel: OpenAiChatModel,
  private val chatClientBuilder: ChatClient.Builder,
) {

  @GetMapping("/{message}")
  fun test(@PathVariable message: String): ResponseEntity<String> {
//    val response = chatModel.call(message)

    val chatClient = chatClientBuilder.build()
    val stringResponse = chatClient.prompt(message).call().content()
    val chatResponse = chatClient.prompt(message).call().chatResponse()
    val metaData = chatResponse!!.metadata
    val text = chatResponse.result.output.text

    return ResponseEntity.ok(stringResponse)
  }
}
