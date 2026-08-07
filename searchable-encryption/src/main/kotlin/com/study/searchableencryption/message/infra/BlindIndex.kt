package com.study.searchableencryption.message.infra

import java.util.HexFormat
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class BlindIndex(
  @Value("\${app.crypto.index-key}") indexKey: String,
) {

  private val key = SecretKeySpec(indexKey.toByteArray(), MAC_ALGORITHM)

  fun tokens(text: String): Set<String> {
    val mac = Mac.getInstance(MAC_ALGORITHM).apply { init(key) }
    val normalizedText = normalize(text)
    return nGrams(normalizedText)
      .map { gram -> hmacHex(mac, gram) }
      .toSet()
  }

  fun normalize(text: String): String = text.lowercase().filter { it.isLetterOrDigit() }

  private fun nGrams(normalized: String): List<String> =
    normalized.windowed(N_GRAM_SIZE).ifEmpty { listOfNotNull(normalized.ifEmpty { null }) }

  private fun hmacHex(mac: Mac, gram: String): String =
    HEX.formatHex(mac.doFinal(gram.toByteArray()), 0, TOKEN_BYTES)

  companion object {
    private val HEX: HexFormat = HexFormat.of()
    private const val MAC_ALGORITHM = "HmacSHA256"
    private const val N_GRAM_SIZE = 2
    private const val TOKEN_BYTES = 16
    const val MIN_QUERY_LENGTH = 2
  }
}
