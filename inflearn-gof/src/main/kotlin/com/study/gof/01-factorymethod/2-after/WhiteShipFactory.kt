package com.study.gof.`01-factorymethod`.`2-after`

import com.study.gof.`01-factorymethod`.Ship

class WhiteShipFactory: ShipFactory {

    override fun createShip(name: String): Ship {
        return WhiteShip(name)
    }
}