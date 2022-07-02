package com.study.gof.factorymethod.`2-after`

import com.study.gof.factorymethod.Ship

class BlackShipFactory: ShipFactory {

    override fun createShip(name: String): Ship {
        return BlackShip(name)
    }
}