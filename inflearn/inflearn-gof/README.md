
## 생성

### Singleton
오직 하나의 인스턴스만을 제공하기 위한 디자인 패턴이다.
전체 애플리케이션에서 싱글턴 클래스의 인스턴스는 하나만 생성되고 해당 인스턴스에 대한 접근이 가능해야 한다.

Singleton 클래스의 조건
- private 생성자
  - `new` 를 통해 새로운 인스턴스가 생기는 것을 막기 위해 유일한 생성자는 private으로 설정한다.
- static 한 인스턴스 접근 메서드
  - `getInstance` 등 인스턴스에 접근 가능한 메서드를 제공한다.

```kotlin
class Settings private constructor() {

  companion object {
    private var instance: Settings? = null

    fun getInstance(): Settings {
      instance = instance ?: Settings()
      return instance as Settings
    }
  }
}

fun main() {
  val settings1 = Settings.getInstance()
  val settings2 = Settings.getInstance()
  val settings3 = Settings.getInstance()
  
  // 모두 같은 인스턴스를 참조
  println(settings1 == settings2)
  println(settings2 == settings3)
  println(settings1 == settings3)
}
```

### Singleton MultiThread (Thread-safe)

`getInstance` 메서드 내부 instance 를 생성하는 부분에서 동시성 이슈가 발생할 수 있다.

`synchronized` 키워드 추가
- getInstance 블럭 자체를 synchronized 하게 동작하도록 한다.
- 메서드 호출 때마다 동기처리(lock)가 추가로 필요하므로 성능상 불이익이 있을 수 있다.

인스턴스 미리 생성
- 미리 인스턴스를 생성한다.
- 인스턴스 생성 비용이 크게 고려되지 않는다면 가장 간단하고 좋은 솔루션이라 생각된다.

### object
kotlin 의 경우 `object` 클래스를 사용하면 쉽게 싱글턴이 적용된 클래스를 만들 수 있다.
`object` 클래스는 그 자체로 인스턴스(오브젝트) 이기 때문에 다른 것을 고려할 필요가 없다.


### Singleton 깨트리기

리플랙션으로 private 한 생성자의 access level 을 변경
```kotlin
val constructor: Constructor<Settings> = Settings::class.java.getDeclaredConstructor()
constructor.trySetAccessible()
val settings4 = constructor.newInstance()
```

직렬화 / 역직렬화 - 역직렬화 시 새로운 인스턴스 생성
```kotlin
val settings5 = Settings.getInstance()
ObjectOutputStream(FileOutputStream("settings.obj")).use {
  it.writeObject(settings5)
}

ObjectInputStream(FileInputStream("settings.obj")).use {
  val settings6 = it.readObject()
  println(settings5 != settings6)
}
```
역질렬화 시 새로운 인스턴스가 생성되는 문제는 readResolve 메서드를 커스텀하여 해결할 수 있다.
readResolve 는 객체 역직렬화시 호출되는 메서드 정도로 알자.

---