package ch1;

import ch1.utils.TripleFunction;

public class Ch3_TripleFunction {
  public static void main(String[] args) {
    TripleFunction<Integer, Integer, Integer, Integer> adder
      = (Integer a, Integer b, Integer c) -> a + b + c;
    System.out.println(adder.apply(10, 20, 30));
  }
}
