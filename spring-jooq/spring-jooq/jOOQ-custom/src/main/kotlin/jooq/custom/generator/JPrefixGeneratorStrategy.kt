package jooq.custom.generator

import org.jooq.codegen.DefaultGeneratorStrategy
import org.jooq.codegen.GeneratorStrategy
import org.jooq.meta.Definition

class JPrefixGeneratorStrategy : DefaultGeneratorStrategy() {

  override fun getJavaClassName(definition: Definition, mode: GeneratorStrategy.Mode): String {
    if (mode == GeneratorStrategy.Mode.DEFAULT) {
      return "J" + super.getJavaClassName(definition, mode)
    }
    return super.getJavaClassName(definition, mode)
  }
}