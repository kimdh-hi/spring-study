package com.study.gof.`abstract-method`.`2-after`

import com.study.gof.`abstract-method`.Ship

class ShipFactory(
    private val shipPartsFactory: ShipPartsFactory
) {

    fun createShip(): Ship {
        return Ship(
            anchor = shipPartsFactory.createAnchor(),
            wheel = shipPartsFactory.createWheel()
        )
    }
}