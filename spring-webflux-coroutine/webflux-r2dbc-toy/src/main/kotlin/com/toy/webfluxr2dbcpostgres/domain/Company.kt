package com.toy.webfluxr2dbcpostgres.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.io.Serial

//@Table(name = "tb_company")
class Company (
  @field:Id
  var id: Long? = null,
  var name: String,
): AbstractByTraceEntity() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 7008385082857824L

  }

}