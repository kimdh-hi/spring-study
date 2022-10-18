package com.toy.springmvc.domain

import com.toy.springmvc.config.NoArg
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
@NoArg
data class Person(
  val name: String
) {
  override fun toString(): String {
    return "Person(name='$name')"
  }
}