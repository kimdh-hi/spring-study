package ch1;

import ch1.utils.Adder;

import java.util.function.Function;

public class Ch1Main {
  public static void main(String[] args) {
//    Function<Integer, Integer> myAdder = new Adder();
    Function<Integer, Integer> myAdder = x -> x + 10;
    Integer result = myAdder.apply(10);
    System.out.println("result = " + result);
  }
}
