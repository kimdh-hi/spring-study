package ch2_stream;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
Predicate 의 결과로 두 개 그룹을 만들어 낼 수 있음
 */
public class Partitioning_by {
  public static void main(String[] args) {
    Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 19);

    Map<Boolean, List<Integer>> partitioningMap = integerStream
      .collect(Collectors.partitioningBy(x -> x % 2 == 0));

    System.out.println(partitioningMap.get(true)); // 짝수
    System.out.println(partitioningMap.get(false)); // 홀수
  }
}
