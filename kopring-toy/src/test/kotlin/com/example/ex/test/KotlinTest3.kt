package com.example.ex.test

class KotlinTest3 {
  val foo = Foo("test")
  val clone = foo.clone() as Foo
}

class Foo(val data: String): Cloneable {
  public override fun clone(): Any {
    return super.clone()
  }
}

