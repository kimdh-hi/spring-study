package ch2_stream;

import java.util.List;
import java.util.stream.IntStream;

public class ForEachWithIndex {
  public static void main(String[] args) {
    List<Integer> integerList = List.of(10,9,8,7,6,5,4,3,2,1,0);
    IntStream.range(0, integerList.size())
      .forEach(i -> System.out.println("index="+i+"value="+integerList.get(i)));
  }
}
