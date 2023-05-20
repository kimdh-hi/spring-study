package com.toy.springswagger_restdocs.base

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension::class)
@Disabled
abstract class AbstractIntegrationTest {

  protected lateinit var mockMvc: MockMvc

  @Autowired private lateinit var context: WebApplicationContext

  @Autowired protected lateinit var objectMapper: ObjectMapper

  @BeforeEach
  fun setup(restDocumentation: RestDocumentationContextProvider) {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
      .apply { documentationConfiguration(restDocumentation) }
      .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
      .build()
  }
}