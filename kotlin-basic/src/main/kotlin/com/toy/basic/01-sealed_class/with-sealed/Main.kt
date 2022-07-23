package com.toy.basic.`01-sealed_class`.`with-sealed`

fun main() {
  getUserStatus(Enabled())
  getUserStatus(Disabled())
  getUserStatus(Warning())
}

/**
 * UserStatus 클래스를 sealed 클래스로 만들어 주는 것은 컴파일러에게 UserStatus의 종류를 알리는 것과 같음
 * 모든 자식클래스에 대한 처리를 강제할 수 있다.
 *
 * 단, sealed 클래스로 선언된 부모 클래스는 같은 패키지 안의 클래스만 상속받을 수 있다.
 */

// else가 없어도 컴파일 에러 xx
// UserStatus 의 자식클래스가 case에 없다면 컴파일에러 발생
fun getUserStatus(userStatus: UserStatus) = when(userStatus) {
  is Enabled -> userStatus.printStatus()
  is Disabled -> userStatus.printStatus()
  is Warning -> userStatus.printStatus()
}