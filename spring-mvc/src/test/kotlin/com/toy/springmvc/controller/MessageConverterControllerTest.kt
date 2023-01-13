package com.toy.springmvc.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.springmvc.domain.Person
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.oxm.Marshaller
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.io.StringWriter
import javax.xml.transform.stream.StreamResult

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class MessageConverterControllerTest(
  private val mockMvc: MockMvc,
  private val objectMapper: ObjectMapper,
  private val marshaller: Marshaller
) {

  @Test
  fun defaultStringMessageConverter() {
    mockMvc.get("/message-converter/message") {
      content = "message"
    }
      .andDo { print() }
      .andExpect {
        content { string("message") }
      }
  }

  @Test
  fun jsonMessageConverter() {
    val person = Person(name = "person")
    mockMvc.get("/message-converter/json-message") {
      contentType = MediaType.APPLICATION_JSON
      content = objectMapper.writeValueAsString(person)
    }
      .andDo { print() }
      .andExpect {
        jsonPath("$.name") { value("person") }
      }
  }

  @Test
  fun xmlMessageConverter() {
    val person = Person(name = "kim")
    val stringWriter = StringWriter()
    val streamResult = StreamResult(stringWriter)
    marshaller.marshal(person, streamResult)
    val xmlString = stringWriter.toString()

    mockMvc.get("/message-converter/xml-message") {
      contentType = MediaType.APPLICATION_XML
      content = xmlString
      accept = MediaType.APPLICATION_XML
    }
      .andDo { print() }
      .andExpect { xpath("person/name") { string("kim")} }
  }
}