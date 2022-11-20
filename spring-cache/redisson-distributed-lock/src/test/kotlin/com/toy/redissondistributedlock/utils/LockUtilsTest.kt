package com.toy.redissondistributedlock.utils

import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class LockUtilsTest(
  private val lockUtils: LockUtils
) {


}