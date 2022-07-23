package com.study.gof.`01-factorymethod`.`1-before`

import com.study.gof.`01-factorymethod`.Ship

/**
 * name 에 따라서 조건분리고 해당하는 Ship을 만들어내고 있음
 * 만들어내야 하는 Ship 의 종류가 많아진다면 분기문으로 가득한 함수가 될 것이고, 요구사항이 추가될 때마다 코드를 고쳐야 함. (OCP 위반)
 *
 *
 */
class ShipFactory {

    companion object {

        fun orderShip(name: String, email: String) : Ship {
            validate(name, email)
            prepare(name)

            val ship = Ship()
            ship.name = name

            // 객체 생성 중 변하는 부분
            setColor(ship, name)
            setLog(ship, name)

            notify(name, email)

            return ship
        }

        private fun notify(name: String, email: String) {
            println("$email : $name 생산완료")
        }

        private fun setLog(ship: Ship, name: String) {
            if (name.equals("whiteship", ignoreCase = true)) {
                ship.log = "\uD83D\uDEA2"
            } else if(name.equals("blackship", ignoreCase = true)) {
                ship.color = "🛳"
            }
        }

        private fun setColor(ship: Ship, name: String) {
            // 요구사항에 따라 다른 생산 방식의 Ship 이 생성되어야 한다면 if-else 구문이 계속해서 늘어나게 됨
            if (name.equals("whiteship", ignoreCase = true)) {
                ship.color = "white"
            } else if(name.equals("blackship", ignoreCase = true)) {
                ship.color = "black"
            }
        }

        private fun validate(name: String, email: String ) {
            if (name.isBlank()) throw IllegalArgumentException("배 이름을 입력하세요.")
            if (email.isBlank()) throw IllegalArgumentException("이메일을 입력하세요.")
        }

        private fun prepare(name: String) {
            println("$name 생산중 ...")
        }
    }
}