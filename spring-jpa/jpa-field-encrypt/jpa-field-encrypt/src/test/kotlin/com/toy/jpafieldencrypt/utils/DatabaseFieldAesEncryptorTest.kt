package com.toy.jpafieldencrypt.utils

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class DatabaseFieldAesEncryptorTest {

  @Test
  fun encrypt() {
    val converter = DatabaseFieldAesEncryptor()
    val encrypted = converter.convertToDatabaseColumn("abc")

    println(encrypted)
  }

  @Test
  fun decrypt() {

    val converter = DatabaseFieldAesEncryptor()
    val encrypted = converter.convertToDatabaseColumn("abc")

    val decrypted = converter.convertToEntityAttribute(encrypted)

    println(decrypted)
  }
}