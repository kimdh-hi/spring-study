package com.study.springgraalvm.nativehint

import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar

class NativeRuntimeHints : RuntimeHintsRegistrar {
  override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
    hints.resources().registerPattern("presets/companies.json")

    listOf(TrimNamePolicy::class.java, UppercaseNamePolicy::class.java).forEach {
      hints.reflection().registerType(
        it,
        MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
        MemberCategory.INVOKE_PUBLIC_METHODS,
      )
    }
  }
}
