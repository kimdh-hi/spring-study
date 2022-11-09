package ch3_funtional_programming_advanced;

import java.util.function.Supplier;

public class Ch3_lazy_evaluation {
  public static void main(String[] args) {

    // || 에 특성에 의해 returnFalse 는 호출되지 않는다.
    if(returnTrue() || returnFalse()) {
      System.out.println(true);
      System.out.println("=====================================");
    }

    // 위와 완전히 같은 로직이지만 returnFalse 가 호출된다.
    // 메서드 호출시 파라미터를 모두 초기화한 후에 내부를 실행하기 때문이다.
    if(or(returnTrue(), returnFalse())) {
      System.out.println(true);
      System.out.println("=====================================");
    }

    if(orLazy(() -> returnTrue(), () -> returnFalse())) {
      System.out.println(true);
    }
  }

  public static boolean or(boolean x, boolean y) {
    return x || y;
  }

  // 파라미터로 Supplier 를 받으면서 함수 호출시 발생하는 초기화작업을 get() 호출시점으로 미룬다.
  public static boolean orLazy(Supplier<Boolean> x, Supplier<Boolean> y) {
    return x.get() || y.get();
  }

  public static boolean returnTrue() {
    System.out.println("returnTrue called...");
    return true;
  }

  public static boolean returnFalse() {
    System.out.println("returnFalse called...");
    return false;
  }
}
