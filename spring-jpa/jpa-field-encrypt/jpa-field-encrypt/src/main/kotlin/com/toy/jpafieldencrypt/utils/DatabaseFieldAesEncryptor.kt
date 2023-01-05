package com.toy.jpafieldencrypt.utils

import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.persistence.AttributeConverter
import javax.persistence.Converter


@Converter
class DatabaseFieldAesEncryptor : AttributeConverter<String, String> {
  override fun convertToDatabaseColumn(text: String): String {
    val cipher = Cipher.getInstance(ALGORITHM)
    val keySpec = SecretKeySpec(KEY, AES)

    cipher.init(Cipher.ENCRYPT_MODE, keySpec, IV_PARAM_SPEC)

    val encrypted = cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8))
    return Base64.getEncoder().encodeToString(encrypted)
  }

  override fun convertToEntityAttribute(text: String): String {
    val cipher = Cipher.getInstance(ALGORITHM)
    val keySpec = SecretKeySpec(KEY, AES)

    cipher.init(Cipher.DECRYPT_MODE, keySpec, IV_PARAM_SPEC)

    val decodedBytes: ByteArray = Base64.getDecoder().decode(text)
    val decrypted = cipher.doFinal(decodedBytes)
    return String(decrypted, StandardCharsets.UTF_8)
  }

  companion object {
    private const val ALGORITHM = "AES/CBC/PKCS5Padding"
    private const val AES = "AES"
    private val KEY = "01234567890123456789012345678901".toByteArray()
    private val IV_PARAM_SPEC = IvParameterSpec(ByteArray(16))
  }
}