package com.study.jwtkeyrotate.common

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.beans.factory.annotation.Value
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

@Converter
class SecretConverter(
  @Value("\${jwt.kek}") kek: String,
) : AttributeConverter<ByteArray, ByteArray> {
  private val key = SecretKeySpec(Base64.getDecoder().decode(kek), "AES")
  private val random = SecureRandom()

  override fun convertToDatabaseColumn(plain: ByteArray): ByteArray {
    val iv = ByteArray(IV_SIZE).also(random::nextBytes)
    return iv + cipher(Cipher.ENCRYPT_MODE, GCMParameterSpec(TAG_BITS, iv)).doFinal(plain)
  }

  override fun convertToEntityAttribute(db: ByteArray): ByteArray =
    cipher(Cipher.DECRYPT_MODE, GCMParameterSpec(TAG_BITS, db, 0, IV_SIZE)).doFinal(db, IV_SIZE, db.size - IV_SIZE)

  private fun cipher(mode: Int, spec: GCMParameterSpec) = Cipher.getInstance("AES/GCM/NoPadding").apply { init(mode, key, spec) }

  companion object {
    private const val IV_SIZE = 12
    private const val TAG_BITS = 128
  }
}
