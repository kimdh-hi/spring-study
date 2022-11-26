package com.sample.`01-ticket`

import java.time.LocalDateTime

class Invitation(
  val `when`: LocalDateTime
)

class Ticket(
  val fee: Long
)

class Bag(
  private var amount: Long,
  private val invitation: Invitation? = null,
  private var ticket: Ticket? = null
) {

  fun hold(ticket: Ticket): Long {
    return if(hasInvitation()) {
      this.ticket = ticket
      0L
    } else {
      this.ticket = ticket
      minusAmount(ticket.fee)
      ticket.fee
    }
  }

  fun hasInvitation() = invitation != null

  fun hasTicket() = ticket != null

  fun minusAmount(amount: Long) {
    this.amount -= amount
  }

  fun plusAmount(amount: Long) {
    this.amount += amount
  }
}

// bag 은 여전히 audience 에 의해 동작하는 수동적인 존재이다.
// bag 도 자율적인 존재로 만들 수 있다.
class Audience(
  private val bag: Bag
) {

  fun buy(ticket: Ticket): Long {
    return bag.hold(ticket)
  }
}

class TicketOffice(
  var amount: Long,
  private val tickets: MutableList<Ticket> = mutableListOf()
) {

  fun sellTicketTo(audience: Audience) {
    plusAmount(audience.buy(getTicket()))
  }

  fun getTicket(): Ticket {
    return tickets.removeAt(0)
  }

  fun minusAmount(amount: Long) {
    this.amount -= amount
  }

  fun plusAmount(amount: Long) {
    this.amount += amount
  }
}

class TicketSeller(
  private val ticketOffice: TicketOffice
) {

  fun sellTo(audience: Audience) {
    ticketOffice.sellTicketTo(audience)
  }
}

class Theater(
  val ticketSeller: TicketSeller
) {
  fun enter(audience: Audience) {
    ticketSeller.sellTo(audience)
  }
}