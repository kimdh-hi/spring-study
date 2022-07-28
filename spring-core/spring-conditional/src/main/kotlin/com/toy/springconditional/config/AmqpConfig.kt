package com.toy.springconditional.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ConditionContext
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ConfigurationCondition
import org.springframework.core.type.AnnotatedTypeMetadata

@Configuration
@Conditional(AmqpConfig.Companion.Condition::class)
class AmqpConfig {

  companion object {
    class Condition: ConfigurationCondition {
      override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean
        = context.environment.getProperty("amqp.enabled", Boolean::class.java) == true

      override fun getConfigurationPhase(): ConfigurationCondition.ConfigurationPhase = ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION

    }
  }

  @Bean
  fun amqpInfo(): AmqpInfo = AmqpInfo()
}

data class AmqpInfo(
  val info:String = "amqpInfo"
)