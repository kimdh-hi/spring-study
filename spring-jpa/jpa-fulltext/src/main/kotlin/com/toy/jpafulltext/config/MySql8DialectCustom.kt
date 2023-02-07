package com.toy.jpafulltext.config

import org.hibernate.dialect.MySQL8Dialect
import org.hibernate.dialect.function.SQLFunctionTemplate
import org.hibernate.type.StandardBasicTypes

class MySql8DialectCustom: MySQL8Dialect() {

  init {
    registerFunction(
      "match",
      SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1) against (?2 in boolean mode)")
    )
  }

}