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

### `Function` - 하나의 매개변수, 반환값
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

### `BiFunction` - 두 개의 매개변수, 반환값
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

### `Supplier` - 반환값만을 가진다
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

### `Consumer` - 하나의 매개변수만을 가진다
```java
@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
}
```
```java
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
```

### `Predicate` - 하나의 매개변수, Boolean 타입 반환
```java
@FunctionalInterface
public interface Predicate<T> {
  boolean test(T t);
}
```
```java
public static void main(String[] args) {
Predicate<Integer> isPositive = x -> x > 0;
System.out.println(isPositive.test(10)); // true
System.out.println(isPositive.test(-1)); // false

List<Integer> list = List.of(1, -1, 0, 10);
System.out.println(filter(list, isPositive));

Predicate<Integer> isNegative = x -> x < 0;
System.out.println(filter(list, isNegative));
}

public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
List<T> result = new ArrayList<>();
for(T item: list) {
  if(predicate.test(item))
    result.add(item);
}
return result;
}
```

### `Comparator`
```java
@FunctionalInterface
public interface Comparator<T> {
  int compare(T o1, T o2);
}
```
반환값이
- 음수면 `o1 > o2`
- 양수면 `o2 > o1`  
- 0이면 `o1 = o2`

파라미터 위치 기준으로 **앞이 작으면 음수, 앞이 크면 양수**

```java
public static void main(String[] args) {
List<User> users = new ArrayList<>();
users.add(new User(1, "kim"));
users.add(new User(3, "lee"));
users.add(new User(2, "park"));

Comparator<User> userIdComparator = (u1, u2) -> u1.getId() - u2.getId(); // id 기준 오름차순 정렬
Collections.sort(users, userIdComparator);
System.out.println(users);

Collections.sort(users, (u1, u2) -> u2.getName().compareTo(u1.getName())); // 이름 기준 내림차순 정렬
System.out.println(users);
}
```

`@FunctionalInterface`
- 하나의 추상 메서드를 가지는 인터페이스 (default, static 메서드는 상관없다.)

---

### Stream Pipeline

구성요소
- 소스: Collection, Array ...
- 중간처리: filter, map ... 
- 종결처리: collect, reduce ...






































 
