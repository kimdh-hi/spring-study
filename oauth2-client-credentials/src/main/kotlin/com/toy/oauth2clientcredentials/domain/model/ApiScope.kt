package com.toy.oauth2clientcredentials.domain.model

enum class ApiScope(val value: String) {
  SERVICE_A("service-a"),
  SERVICE_B("service-b"),
  ;

  val authority get() = "SCOPE_$value"

  companion object {
    private val values = entries.associateBy { it.value }

    fun isSupported(value: String) = value in values
  }
}
