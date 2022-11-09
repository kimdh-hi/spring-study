package ch2_stream;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Collector {
  public static void main(String[] args) {
    List<Integer> list = Stream.of(5, 4, 3, -1, -2, 10)
      .collect(Collectors.toList());
    System.out.println(list);

    Set<Integer> set = Stream.of(1, 1, 2, 2, 3, 3)
      .collect(Collectors.toSet());
    System.out.println(set);

    List<Integer> absList = Stream.of(-5, -4, 3, -1, -2, 10)
      .collect(Collectors.mapping(x -> Math.abs(x), Collectors.toList()));
    System.out.println(absList);
  }
}
