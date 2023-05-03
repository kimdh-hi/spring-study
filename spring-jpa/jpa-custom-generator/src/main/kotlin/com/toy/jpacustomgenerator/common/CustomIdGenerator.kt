package com.toy.jpacustomgenerator.common

import com.fasterxml.uuid.Generators
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import org.slf4j.LoggerFactory
import java.io.Serializable

class CustomIdGenerator: IdentifierGenerator {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun generate(session: SharedSessionContractImplementor, entity: Any): Serializable {
    val id = Generators.timeBasedEpochGenerator().generate().toString()
    log.info("uuid generating... id: {}", id)
    return id
  }

}