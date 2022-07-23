package com.study.gof.`01-factorymethod`.`2-after`

/**
 * 팩토리 메서드
 * 같은 타입의 인스턴스를 다른 방식으로 만들어야 할 때 기존 코드를 변경하지 않고 적용할 때 사용
 */
fun main(args: Array<String>) {

    val whiteShip = WhiteShipFactory().orderShip("whiteship", "white@gmail.com")
    println(whiteShip)

    val blackShip = BlackShipFactory().orderShip("blackship", "black@gmail.com")
    println(blackShip)
}
