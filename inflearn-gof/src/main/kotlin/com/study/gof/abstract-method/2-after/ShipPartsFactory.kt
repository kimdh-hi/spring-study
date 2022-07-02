package com.study.gof.`abstract-method`.`2-after`

interface ShipPartsFactory {

    fun createAnchor(): Anchor

    fun createWheel(): Wheel
}

class WhiteShipPartsFactory: ShipPartsFactory {

    override fun createAnchor(): Anchor {
        return WhiteShipAnchor()
    }

    override fun createWheel(): Wheel {
        return WhiteShipWheel()
    }
}

class WhiteShipAnchor: Anchor {}

class WhiteShipWheel: Wheel {}