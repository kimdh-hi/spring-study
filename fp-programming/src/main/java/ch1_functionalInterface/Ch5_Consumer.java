package ch1_functionalInterface;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Ch5_Consumer {
  public static void main(String[] args) {
    Consumer<String> myConsumer = (str) -> System.out.println(str);
    myConsumer.accept("test");

    Consumer myConsumer2 = item -> System.out.println("some logic applied.. " + item);
    Consumer myConsumer3 = item -> System.out.println("some different logic applied.. " + item);

    List<Integer> list = Arrays.asList(1,2,3,4,5);
    process(list, myConsumer2);
    process(list, myConsumer3);

    List<Double> doubleList = Arrays.asList(1.1,2.2,3.3,4.4,5.5);
    process(doubleList, myConsumer2);
  }

  public static <T> void process(List<T> list, Consumer<T> consumer) {
    for(T item: list) {
      consumer.accept(item);
    }
  }
}
