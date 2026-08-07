package com.study.searchableencryption.message.infra

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.stereotype.Component

@Component
@Converter
class EncryptedStringConverter(
  private val textEncryptor: TextEncryptor,
) : AttributeConverter<String?, String?> {

  override fun convertToDatabaseColumn(attribute: String?): String? =
    attribute?.let(textEncryptor::encrypt)

  override fun convertToEntityAttribute(dbData: String?): String? =
    dbData?.let(textEncryptor::decrypt)
}
