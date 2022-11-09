package ch3_funtional_programming_advanced;

import java.util.function.Supplier;

/*
함수 안에 함수가 있는 경우 내부 함수에서 외부 함수의 변수에 접근 가능하다.
원래 함수의 경우 호출된 후 메모리에서 함수에 선언된 변수를 지운다. (스택 메모리)

하지만 함수 안에 함수가 있고 해당 함수가 외부 함수의 변수를 필요로 한다면 외부함수에 선언되 변수는 지워져선 안 된다.

단, 이 때 외부 함수의 변수는 final 로 취급된다
 */
public class Ch1_lexical_scope {
  public static void main(String[] args) {
    Supplier<String> someString = getSomeString(); // 보통 함수의 호출이 끝난 경우 함수 내부의 변수는 메모리에서 지워진다.
    System.out.println(someString.get()); // hello world 출력. 외부함수의 변수가 메모리에서 지워지지 않고 사용되고 있다.
  }

  public static Supplier<String> getSomeString() {
    String hello = "hello";
    Supplier<String> supplier =  () -> {
     String world = " world"; // 내부함수에서 외부 함수의 변수를 사용하고 있음
     return hello + world;
    };
    return supplier;
  }
}
