package com.sample.`01-ticket`//package com.sample
//
//import java.time.LocalDateTime
//
//class Invitation(
//  val `when`: LocalDateTime
//)
//
//class Ticket(
//  val fee: Long
//)
//
//class Bag(
//  var amount: Long,
//  val invitation: Invitation? = null,
//  var ticket: Ticket? = null
//) {
//  fun hasInvitation() = invitation != null
//
//  fun hasTicket() = ticket != null
//
//  fun minusAmount(amount: Long) {
//    this.amount -= amount
//  }
//
//  fun plusAmount(amount: Long) {
//    this.amount += amount
//  }
//}
//
///**
// * audience 자신의 bag 에 접근해서 티켓이 있는지 확인하고 금액을 지불하는 것은 자기 자신의 책임이다.
// */
//class Audience(
//  private val bag: Bag // audience 의 bag 은 private 으로 외부에서 접근을 막았다. (캡슐화)
//) {
//  fun buy(ticket: Ticket): Long {
//    return if (bag.hasInvitation()) {
//      bag.ticket = ticket
//      0L
//    } else {
//      bag.ticket = ticket
//      bag.minusAmount(ticket.fee)
//      ticket.fee
//    }
//  }
//}
//
//class TicketOffice(
//  var amount: Long,
//  val tickets: MutableList<Ticket> = mutableListOf()
//) {
//  fun getTicket(): Ticket {
//    return tickets.removeAt(0)
//  }
//
//  fun minusAmount(amount: Long) {
//    this.amount -= amount
//  }
//
//  fun plusAmount(amount: Long) {
//    this.amount += amount
//  }
//}
//
//class TicketSeller(
//  private val ticketOffice: TicketOffice // TicketSeller 의 ticketOffice 은 private 으로 외부에서 접근을 막았다. (캡슐화)
//) {
//
//  fun sellTo(audience: Audience) {
//    ticketOffice.plusAmount(audience.buy(ticketOffice.getTicket()))
//  }
//}
//
//class Theater(
//  val ticketSeller: TicketSeller
//) {
//  fun enter(audience: Audience) {
//    ticketSeller.sellTo(audience)
//  }
//}