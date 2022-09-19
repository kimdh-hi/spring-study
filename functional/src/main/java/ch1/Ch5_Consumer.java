package ch1;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Ch5_Consumer {
  public static void main(String[] args) {
    Consumer<String> myConsumer = (str) -> System.out.println(str);
    myConsumer.accept("test");

    Consumer myConsumer2 = item -> System.out.println("some logic applied.. " + item);
    List<Integer> list = Arrays.asList(1,2,3,4,5);
    process(list, myConsumer2);
  }

  public static void process(List<Integer> list, Consumer<Integer> consumer) {
    for(Integer item: list) {
      consumer.accept(item);
    }
  }
}
