package com.toy.springswagger_restdocs.base

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension::class)
abstract class AbstractIntegrationTest {

  protected lateinit var mockMvc: MockMvc

  @Autowired protected lateinit var objectMapper: ObjectMapper

  @BeforeEach
  fun setup(context: WebApplicationContext, restDocumentation: RestDocumentationContextProvider) {
//    mockMvc = MockMvcBuilders.webAppContextSetup(context)
//      .apply { documentationConfiguration(restDocumentation) }
//      .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
//      .alwaysDo<DefaultMockMvcBuilder>(print())
//      .build()

    mockMvc = MockMvcBuilders.webAppContextSetup(context)
      .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
      .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
      .alwaysDo<DefaultMockMvcBuilder>(print())
      .build()
  }
}