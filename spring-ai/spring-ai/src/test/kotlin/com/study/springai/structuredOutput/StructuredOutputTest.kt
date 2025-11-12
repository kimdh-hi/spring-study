package com.study.springai.structuredOutput

import com.study.springai.common.SimpleLoggerAdvisor
import org.junit.jupiter.api.Test
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.converter.BeanOutputConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference

@SpringBootTest
@AutoConfigureMockMvc
class StructuredOutputTest @Autowired constructor(
  private val chatClientBuilder: ChatClient.Builder,
) {

  private val chatClient = chatClientBuilder
    .defaultAdvisors(SimpleLoggerAdvisor())
    .build()

  private val country = "대한민국"

  @Test
  fun test() {
    val outputConverter = BeanOutputConverter(
      object : ParameterizedTypeReference<List<President>>() {}
    )

    val promptTemplate = PromptTemplate("$country 의 역대 대통령 5명을 출력하세요. {format}")
    val prompt = promptTemplate.create(mapOf("format" to outputConverter.format))

    val content = chatClient
      .prompt(prompt)
      .advisors { SimpleLoggerAdvisor() }
      .options(
        ChatOptions.builder()
          .maxTokens(200)
          .build()
      )
      .call()
      .content()!!
    val presidents = outputConverter.convert(content)

    println(presidents)
  }

  @Test
  fun test2() {
    val presidents = chatClient
      .prompt("$country 의 역대 대통령 5명을 출력하세요.")
      .options(
        ChatOptions.builder()
          .maxTokens(200)
          .build()
      )
      .call()
      .entity(object : ParameterizedTypeReference<List<President>>() {})

    println(presidents)
  }

}

private data class President(
  val country: String,
  val `이름`: String,
)
