package com.toy.springxssprotection

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractBaseTest