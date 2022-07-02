package com.study.gof.factorymethod

open class Ship(
    var name: String? = null,
    var color: String? = null,
    var log: String? = null) {

    override fun toString(): String {
        return "Ship(name=$name, color=$color, log=$log)"
    }
}