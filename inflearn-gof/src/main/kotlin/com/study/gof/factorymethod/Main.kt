package com.study.gof.factorymethod

import com.study.gof.factorymethod.ex1.ShipFactory

/**
 * 새로운 유형의 ship을 만들려고 한다면 객체 생성 메서드 자체를 변경해야 한다. (OCP 위배)
 */
fun main(args: Array<String>) {
    val whiteShip = ShipFactory.orderShip("white", "aaa@gmail.com")
    val blackShip = ShipFactory.orderShip("black", "bbb@gmail.com")
}
