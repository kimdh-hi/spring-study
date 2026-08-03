package com.study.springgraalvm.nativehint

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints

@Configuration
@ImportRuntimeHints(NativeRuntimeHints::class)
class NativeHintsConfig
