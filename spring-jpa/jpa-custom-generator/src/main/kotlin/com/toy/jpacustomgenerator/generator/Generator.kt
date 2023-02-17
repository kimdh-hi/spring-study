package com.toy.jpacustomgenerator.generator

import com.fasterxml.uuid.Generators
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import java.io.Serializable

class CustomIdGenerator: IdentifierGenerator {

  override fun generate(session: SharedSessionContractImplementor, entity: Any): Serializable {
    return Generators.timeBasedEpochGenerator().generate().toString()
  }

}