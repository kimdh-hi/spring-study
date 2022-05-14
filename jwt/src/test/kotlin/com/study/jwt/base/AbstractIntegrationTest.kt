package com.study.jwt.base

import com.fasterxml.jackson.databind.ObjectMapper
import com.study.jwt.auth.JwtPrincipal
import com.study.jwt.auth.JwtUtil
import com.study.jwt.repository.AccountRepository
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
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext

@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
abstract class AbstractIntegrationTest() {

    val BEARER_PREFIX = "bearer "

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired private lateinit var context: WebApplicationContext
    @Autowired lateinit var objectMapper: ObjectMapper
    @Autowired lateinit var accountRepository: AccountRepository

    lateinit var userToken: String
    lateinit var adminToken: String

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder?>(SecurityMockMvcConfigurers.springSecurity())
            .alwaysDo<DefaultMockMvcBuilder?>(MockMvcResultHandlers.print())
            .build()

        val user = accountRepository.findById(TestData.ACCOUNT_USER_ID).get()
        userToken = JwtUtil.generateToken(JwtPrincipal.fromAccount(user))

        val admin = accountRepository.findById(TestData.ACCOUNT_ADMIN_ID).get()
        adminToken = JwtUtil.generateToken(JwtPrincipal.fromAccount(admin))
    }
}