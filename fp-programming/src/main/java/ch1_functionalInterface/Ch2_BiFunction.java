package ch1_functionalInterface;

import java.util.function.BiFunction;

public class Ch2_BiFunction {
  public static void main(String[] args) {
    BiFunction<Integer, Integer, Integer> adder = (a, b) -> a + b;
    Integer result = adder.apply(10, 20);
    System.out.println(result);
  }
}
