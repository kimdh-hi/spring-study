package com.lecture.inflearnspringsecurityoauth2.config.converter

interface ProviderUserConverter<T, R> {
  fun convert(t: T): R?
}