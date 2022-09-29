package com.toy.restdocsdemo.base

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
abstract class AbstractBaseTest {

  protected lateinit var mockMvc: MockMvc

  @BeforeEach
  fun setup(
    webApplicationContext: WebApplicationContext,
    restDocumentationContextProvider: RestDocumentationContextProvider
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