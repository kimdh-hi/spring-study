package com.toy.basic.`01-sealed_class`.`without-sealed`

fun main() {
  getUserStatus(Enabled())
  getUserStatus(Disabled())
  getUserStatus(Warning())
}

fun getUserStatus(userStatus: UserStatus) = when(userStatus) {
  is Enabled -> userStatus.printStatus()
  is Disabled -> userStatus.printStatus()
  is Warning -> userStatus.printStatus()
  else -> println("Unknown status ...") // else 가 없을 시 컴파일 에러 발생 ...
}