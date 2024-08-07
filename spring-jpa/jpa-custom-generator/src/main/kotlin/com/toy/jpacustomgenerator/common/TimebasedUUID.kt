package com.toy.jpacustomgenerator.common

import org.hibernate.id.IdentifierGenerator
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class TimebasedUUID(
  val generator: KClass<out IdentifierGenerator>
)
