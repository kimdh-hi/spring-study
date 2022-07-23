package com.study.gof.`01-factorymethod`.`2-after`

import com.study.gof.`01-factorymethod`.Ship

class BlackShipFactory: ShipFactory {

    override fun createShip(name: String): Ship {
        return BlackShip(name)
    }
}