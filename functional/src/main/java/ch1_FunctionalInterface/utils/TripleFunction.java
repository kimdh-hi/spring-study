package ch1_FunctionalInterface.utils;

@FunctionalInterface
public interface TripleFunction<T, U, V, R> {
  R apply(T t, U u, V v);
}
