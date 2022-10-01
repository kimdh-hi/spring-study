package ch2_stream;

import java.util.Optional;
import java.util.stream.Stream;

public class Ch3_find_first_any {
  public static void main(String[] args) {
    /**
     * findFirst
     */
    Optional<Integer> firstFindData = Stream.of(1, 2, 3, -1, 4, 5, -2, -4, 6)
      .filter(x -> x < 0)
      .findFirst();
    System.out.println(firstFindData.get());

    /**
     * findAny
     * Stream 내 조건에 맞는 아무 데이터를 반환한다.
     * parallel 로 stream 을 사용할 때 findFirst 보다 좋은 성능을 기대할 수 있다. 하지만 순서를 보장하지 않는다.
     */
    Optional<Integer> firstAnyData = Stream.of(1, 2, 3, -1, 4, 5, -2, -4, 6)
      .filter(x -> x < 0)
      .parallel()
      .findAny();
    System.out.println(firstAnyData.get());
  }
}
