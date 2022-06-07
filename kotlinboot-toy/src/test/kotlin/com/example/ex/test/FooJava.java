package com.example.ex.test;

public class FooJava implements Cloneable {

  private String data;

  public FooJava(String data) {
    this.data = data;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
