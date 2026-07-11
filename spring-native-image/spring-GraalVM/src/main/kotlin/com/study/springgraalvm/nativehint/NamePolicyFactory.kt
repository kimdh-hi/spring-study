package com.study.springgraalvm.nativehint

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class NamePolicyFactory(
  @param:Value("\${app.name-policy}") fqcn: String,
) {
  val policy: NamePolicy =
    Class.forName(fqcn).getDeclaredConstructor().newInstance() as NamePolicy
}
