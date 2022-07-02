package com.study.gof.factorymethod.`2-after`

import com.study.gof.factorymethod.Ship

class WhiteShipFactory: ShipFactory {

    override fun createShip(name: String): Ship {
        return WhiteShip(name)
    }
}