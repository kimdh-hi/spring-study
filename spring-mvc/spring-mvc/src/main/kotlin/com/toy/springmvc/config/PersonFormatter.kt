package com.toy.springmvc.config

import com.toy.springmvc.controller.Person
import org.springframework.format.Formatter
import org.springframework.stereotype.Component
import java.util.*

@Component
class PersonFormatter: Formatter<Person> {

  override fun print(person: Person, locale: Locale): String {
    return person.toString()
  }

  override fun parse(text: String, locale: Locale): Person {
    val person = Person(name = text)
    return person
  }
}