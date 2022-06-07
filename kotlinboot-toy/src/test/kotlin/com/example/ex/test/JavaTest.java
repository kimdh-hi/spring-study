package com.example.ex.test;

public class JavaTest {
  FooJava foo = new FooJava("test");
  FooJava clone;

  {
    try {
      clone = foo.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
  }
}
