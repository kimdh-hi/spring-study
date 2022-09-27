package ch3_funtional_programming_advanced;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Ch2_curry {
  public static void main(String[] args) {
    BiFunction<Integer, Integer, Integer> adder = (x, y) -> x + y;
    System.out.println(adder.apply(1, 2));

    Function<Integer, Function<Integer, Integer>> curriedAdder = x -> y -> x + y;
    Function<Integer, Integer> addThree = curriedAdder.apply(3);
    System.out.println(addThree.apply(10));

  }
}
