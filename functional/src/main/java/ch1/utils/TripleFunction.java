package ch1.utils;

@FunctionalInterface
public interface TripleFunction<T, U, V, R> {
  R apply(T t, U u, V v);
}
