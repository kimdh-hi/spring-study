package ch2_stream;

/*
중간과정은 병렬로 처리되지만 종결처리에 의해 순서가 보장된다. (collect 등의 종결처리)
여러 Thread 가 중간처리를 병렬로 처리하면서 성능향상이 가능하다

단, parallelStream 이 항상 성능향상을 가져오는 것은 아니다.
오히려 deadlock 등 오류가 발생할 위험이 있다.
이런 병렬처리가 주는 위험을 피하기 위해 뮤텍스, 세마포어 등 기술을 함께 사용하는데
이로 인해 오히려 속도가 느려질 수 있따.
 */
public class ParallelStream {
  public static void main(String[] args) {

  }
}
