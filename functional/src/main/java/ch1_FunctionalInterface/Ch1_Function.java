package ch1_FunctionalInterface;

import java.util.function.Function;

public class Ch1_Function {
  public static void main(String[] args) {
//    Function<Integer, Integer> myAdder = new Adder();
    Function<Integer, Integer> myAdder = x -> x + 10;
    Integer result = myAdder.apply(10);
    System.out.println("result = " + result);
  }
}