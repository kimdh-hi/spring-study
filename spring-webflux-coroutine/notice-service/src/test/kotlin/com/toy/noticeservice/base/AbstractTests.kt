package com.toy.noticeservice.base

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractBaseTest

@DataR2dbcTest
@Import(RepositoryConfiguration::class)
abstract class AbstractRepositoryTest: AbstractBaseTest()

@SpringBootTest
abstract class AbstractIntegrationTest: AbstractBaseTest() {

}