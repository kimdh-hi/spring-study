# Functional Programming


## 1급 시민 함수

1급 시민의 함수의 조건
- 함수의 매개변수로서 전달될 수 있다.
- 함수의 반환값이 될 수 있다.
- 변수에 함수를 담을 수 있다.

java와 같은 일반적인 OOP 에서 1급 시민은 `Object` 이다.
매개변수로 전달 가능하고 반환될 수 있으며 변수에 담을 수 있기 때문이다.

OOP에서 1급 시민으로서의 함수를 사용하려면 함수를 `Object` 의 형태로 나타내야 한다.

Functional Interface 를 사용해서 함수를 `Object` 형태로 나타내는 것이 가능하다.

---

## Functional Interface

`Function` - 하나의 매개변수, 반환값
```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);
}
```
```java
Function<Integer, Integer> myAdder = x -> x + 10;
Integer result = myAdder.apply(10);
System.out.println("result = " + result);
```

`BiFunction` - 두 개의 매개변수, 반환값
```java
@FunctionalInterface
public interface BiFunction<T, U, R> {
    R apply(T t, U u);
}
```

```java
BiFunction<Integer, Integer, Integer> adder = (a, b) -> a + b;
Integer result = adder.apply(10, 20);
System.out.println(result);
```

`Supplier` - 반환값만을 가진다
```java
@FunctionalInterface
public interface Supplier<T> {
    T get();
}
```

```java
Supplier<Double> randomDoubleValueGenerator = () -> Math.random();
System.out.println(randomDoubleValueGenerator.get());
System.out.println(randomDoubleValueGenerator.get());
System.out.println(randomDoubleValueGenerator.get());
```

`Consumer` - 하나의 매개변수만을 가진다
```java
@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
}
```

`@FunctionalInterface`
- 하나의 추상 메서드를 가지는 인터페이스 (default, static 메서드는 상관없다.)








































 
