package com.study.springboot4.config

import org.springframework.beans.factory.BeanRegistrarDsl

class ProgrammaticBeanRegistrar : BeanRegistrarDsl({
  registerBean(
    name = "bar",
    prototype = true,
  ) {
    Bar()
  }

  registerBean(name = "foo1") {
    Foo(bean<Bar>(), "foo1 data")
  }

  registerBean(name = "foo2") {
    Foo(bean<Bar>(), "foo2 data")
  }

//  profile("test") {
//    registerBean(name = "foo3") {
//      Foo(bean<Bar>(), "foo3 data")
//    }
//  }
})

class Foo(val bar: Bar, val data: String)

class Bar
