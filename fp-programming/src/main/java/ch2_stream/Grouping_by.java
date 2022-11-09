package ch2_stream;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
Function 의 반환값이 key 값이 되고 Function 의 입력값은 해당 key 의 값이 된다.
 */
public class Grouping_by {
  public static void main(String[] args) {
    // 3으로 나눈 나머지는 key로 한다
    List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    Map<Integer, List<Integer>> groupingMap = list.stream()
      .collect(Collectors.groupingBy(x -> x % 3));
    System.out.println(groupingMap);

    List<Integer> list2 = List.of(123, 234, 634, 140, 299);
    Map<Integer, List<Integer>> groupingMap2 = list2.stream()
      .collect(Collectors.groupingBy(x -> x % 10));
    System.out.println(groupingMap2);

    List<Integer> list3 = List.of(123, 234, 634, 140, 299);
    Map<Integer, Set<Integer>> groupingMap3 = list3.stream()
      .collect(Collectors.groupingBy(x -> x % 10, Collectors.toSet()));
    System.out.println(groupingMap3);
  }
}
