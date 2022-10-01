package ch2_stream;

import java.util.stream.Stream;

public class Ch2_allMatch_anyMatch {
  public static void main(String[] args) {
    /**
     * allMatch: Stream 내 모든 요소가 predicate true 조건을 만족하는지
     */
    boolean result1 = Stream.of(2, 4, 6, 8, 10)
      .allMatch(x -> x % 2 == 0);
    System.out.println(result1);

    boolean result2 = Stream.of(2, 4, 6, 8, 10, 11)
      .allMatch(x -> x % 2 == 0);
    System.out.println(result2);

    /**
     * anyMatch: Stream 내 요소 중 하나라도 predicate true 조건을 만족하는지
     */
    boolean result3 = Stream.of(2, 4, 6, 8, 10,11)
      .anyMatch(x -> x % 2 == 0);
    System.out.println(result3);
  }
}
