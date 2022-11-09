package ch1_functionalInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Ch6_Predicate {
  public static void main(String[] args) {
    Predicate<Integer> isPositive = x -> x > 0;
    System.out.println(isPositive.test(10)); // true
    System.out.println(isPositive.test(-1)); // false

    List<Integer> list = List.of(1, -1, 0, 10);
    System.out.println(filter(list, isPositive));

    Predicate<Integer> isNegative = x -> x < 0;
    System.out.println(filter(list, isNegative));
  }

  public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
    List<T> result = new ArrayList<>();
    for(T item: list) {
      if(predicate.test(item))
        result.add(item);
    }
    return result;
  }
}
