package com.toy.basic.`01-sealed_class`.`with-sealed`

sealed class UserStatus {
  abstract fun printStatus()
}

class Enabled: UserStatus() {
  override fun printStatus() = println("Enabled !!")
}

class Disabled: UserStatus() {
  override fun printStatus() = println("Disabled !!")
}

class Warning: UserStatus() {
  override fun printStatus() = println("Warning !!")
}

