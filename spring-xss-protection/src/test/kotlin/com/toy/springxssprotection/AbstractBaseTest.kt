package com.toy.springxssprotection

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractBaseTest {

  @Autowired
  private lateinit var context: WebApplicationContext

  protected lateinit var mockMvc: MockMvc

  @BeforeEach
  fun setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
      .apply<DefaultMockMvcBuilder?>(SecurityMockMvcConfigurers.springSecurity())
      .alwaysDo<DefaultMockMvcBuilder?>(MockMvcResultHandlers.print())
      .build()
  }
}