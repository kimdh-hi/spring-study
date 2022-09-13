package com.study.gof.`01-생성`.`00-singleton`

import java.io.*
import java.lang.reflect.Constructor

open class Settings private constructor(): Serializable {

  companion object {
    @Serial
    private const val serialVersionUID: Long = -1647791035431899381L

    private var instance: Settings? = null

    fun getInstance(): Settings {
      instance = instance ?: Settings()
      return instance as Settings
    }
  }

  // 역직렬화 시 호출되는 메서드
  // readResolve 가 없는 경우 생성자를 통해 새로운 인스턴스를 생성한다.
//  protected fun readResolve(): Any {
//    return getInstance()
//  }
}

enum class SettingsEnum {
  INSTANCE
}

fun main() {
  val settings1 = Settings.getInstance()
  val settings2 = Settings.getInstance()
  val settings3 = Settings.getInstance()

  println(settings1 == settings2)
  println(settings2 == settings3)
  println(settings1 == settings3)
  println("=================")

  // 싱글턴 깨기 - 리플랙션
  val constructor: Constructor<Settings> = Settings::class.java.getDeclaredConstructor()
  constructor.trySetAccessible()
  val settings4 = constructor.newInstance()
  println(settings1 != settings4)
  println("=================")

  // 싱글턴 깨기 - 직렬화/역직렬화
  val settings5 = Settings.getInstance()
  ObjectOutputStream(FileOutputStream("settings.obj")).use {
    it.writeObject(settings5)
  }
  // 역직렬화시 새로운 인스턴스가 생성된다.
  ObjectInputStream(FileInputStream("settings.obj")).use {
    val settings6 = it.readObject()
    println(settings5 != settings6)
  }

}