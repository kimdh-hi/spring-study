package com.study.jwtkeyrotate.common

import java.util.Base64
import javax.crypto.AEADBadTagException
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class SecretConverterTest {
  private val converter = SecretConverter(Base64.getEncoder().encodeToString(ByteArray(32) { it.toByte() }))
  private val plain = "secret-secret-secret-secret-1234".toByteArray()

  @Test
  fun `암복호화 왕복`() {
    val encrypted = converter.convertToDatabaseColumn(plain)
    assertFalse(encrypted.contentEquals(plain))
    assertContentEquals(plain, converter.convertToEntityAttribute(encrypted))
  }

  @Test
  fun `변조된 암호문은 복호화 실패`() {
    val encrypted = converter.convertToDatabaseColumn(plain).also { it[it.size - 1] = (it.last() + 1).toByte() }
    assertFailsWith<AEADBadTagException> { converter.convertToEntityAttribute(encrypted) }
  }

  @Test
  fun `다른 KEK로는 복호화 실패`() {
    val other = SecretConverter(Base64.getEncoder().encodeToString(ByteArray(32)))
    assertFailsWith<AEADBadTagException> { other.convertToEntityAttribute(converter.convertToDatabaseColumn(plain)) }
  }
}
