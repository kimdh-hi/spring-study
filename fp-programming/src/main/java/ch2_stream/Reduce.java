package ch2_stream;

import java.util.List;

public class Reduce {
  public static void main(String[] args) {
    List<Integer> list = List.of(1 ,-5, 3, -2, 10);
    // f(f(f(f(f(1),-5),3),-2),10)
    int sum = list.stream()
      .reduce((x, y) -> x + y)
      .get();
    System.out.println(sum);

    // reduce 로 최소값 찾기
    // reduce 의 처리방식을 알 수 있음 (마치 버블정렬같은 느낌?)
     int min = list.stream()
       .reduce((x, y) -> {
         if (x > y)
           return y;
         return x;
       })
       .get();
    System.out.println(min);
  }
}
