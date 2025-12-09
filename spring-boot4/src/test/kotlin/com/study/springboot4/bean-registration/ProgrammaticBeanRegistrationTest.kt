package com.study.springboot4.`bean-registration`

import com.study.springboot4.config.ProgrammaticBeanRegistrar
import com.study.springboot4.config.Foo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(ProgrammaticBeanRegistrar::class)
class ProgrammaticBeanRegistrationTest {

  @Autowired
  @Qualifier("foo1")
  private lateinit var foo1: Foo

  @Autowired
  @Qualifier("foo2")
  private lateinit var foo2: Foo

  @Test
  fun test() {
    val bar1 = foo1.bar
    val bar2 = foo2.bar
    assertThat(foo1.data).isEqualTo("foo1 data")
    assertThat(foo2.data).isEqualTo("foo2 data")
    assertThat(bar1).isNotSameAs(bar2)
  }

}

