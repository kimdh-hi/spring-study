package ch3_funtional_programming_advanced;

import java.util.function.Function;

/*
Function<V, R> compose(Function<V,T> before)
Function<T, V> andThen(Function<R,V> after)

합성시 가독성 측면에서 andThen 권장
 */
public class Ch4_function_composition {
  public static void main(String[] args) {
    Function<Integer, Integer> multiplyTen = x -> x * 10;
    Function<Integer, Integer> addTen = x -> x + 10;

    Function<Integer, Integer> composedFunction = multiplyTen.andThen(addTen);
    Integer result = composedFunction.apply(5);

    System.out.println(result);
  }
}
