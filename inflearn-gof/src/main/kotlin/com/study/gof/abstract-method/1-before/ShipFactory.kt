package com.study.gof.`abstract-method`.`1-before`

import com.study.gof.`abstract-method`.Anchor
import com.study.gof.`abstract-method`.Ship
import com.study.gof.`abstract-method`.Wheel


class ShipFactory {

    fun createShip(): Ship {
        val ship = Ship(
            anchor = Anchor(),
            wheel = Wheel()
        )
        return ship
    }
}