package com.study.gof.`01-factorymethod`.`2-after`

import com.study.gof.`01-factorymethod`.Ship

interface ShipFactory {

    fun orderShip(name: String, email: String): Ship {
        validate(name, email)
        prepare(name)

        val ship = createShip(name)

        notify(name, email)

        return ship
    }

    fun createShip(name: String): Ship

    private fun validate(name: String, email: String ) {
        if (name.isBlank()) throw IllegalArgumentException("배 이름을 입력하세요.")
        if (email.isBlank()) throw IllegalArgumentException("이메일을 입력하세요.")
    }

    private fun prepare(name: String) {
        println("$name 생산중 ...")
    }

    private fun notify(name: String, email: String) {
        println("$email : $name 생산완료")
    }
}