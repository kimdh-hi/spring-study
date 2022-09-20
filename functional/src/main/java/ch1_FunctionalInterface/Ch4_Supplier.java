package ch1_FunctionalInterface;

import java.util.function.Supplier;

public class Ch4_Supplier {
  public static void main(String[] args) {
    Supplier<String> mySupplier = () -> "supplier...";
    System.out.println(mySupplier.get());


  }
}
