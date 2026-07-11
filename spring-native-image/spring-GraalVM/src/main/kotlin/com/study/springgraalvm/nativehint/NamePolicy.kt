package com.study.springgraalvm.nativehint

interface NamePolicy {
  fun apply(name: String): String
}

class TrimNamePolicy : NamePolicy {
  override fun apply(name: String): String = name.trim()
}

class UppercaseNamePolicy : NamePolicy {
  override fun apply(name: String): String = name.trim().uppercase()
}
