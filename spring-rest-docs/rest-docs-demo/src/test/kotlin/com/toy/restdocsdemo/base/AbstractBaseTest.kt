package com.toy.restdocsdemo.base

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@SpringBootTest
@AutoConfigureRestDocs
@Import(RestDocsConfiguration::class)
@ExtendWith(RestDocumentationExtension::class)
@Transactional
abstract class AbstractBaseTest {

  protected lateinit var mockMvc: MockMvc

  @BeforeEach
  fun setup(
    webApplicationContext: WebApplicationContext,
    restDocumentationContextProvider: RestDocumentationContextProvider,
  ) {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
      .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
      .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
      .apply<DefaultMockMvcBuilder>(
        MockMvcRestDocumentation.documentationConfiguration(
          restDocumentationContextProvider
        )
      )
      .build()
  }
}

@TestConfiguration
class RestDocsConfiguration {
  @Bean
  fun write(): RestDocumentationResultHandler {
    return MockMvcRestDocumentation.document(
      "{class-name}/{method-name}",
      Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
      Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
    )
  }
}