package ch1_functionalInterface;

import java.util.function.Supplier;

public class Ch4_Supplier {
  public static void main(String[] args) {
    Supplier<String> mySupplier = () -> "supplier...";
    System.out.println(mySupplier.get());


  }
}
