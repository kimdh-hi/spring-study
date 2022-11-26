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
//class Audience(
//  val bag: Bag
//)
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
//  val ticketOffice: TicketOffice
//)
//
///**
// * audience 와  audience 의 bag, ticketSeller 와 ticketSeller 의 ticketOffice 까지 theater 가 직접 접근하며 통제하고 변경하고 있다.
// * audience 가 변경되는 ticketSeller 가 변경되는 이와 결합된 Theater 까지 변경될 수 있다.
// */
//class Theater(
//  val ticketSeller: TicketSeller
//) {
//  fun enter(audience: Audience) {
//    if (audience.bag.hasInvitation()) {
//      val ticket = ticketSeller.ticketOffice.getTicket()
//      audience.bag.ticket = ticket
//    } else {
//      val ticket = ticketSeller.ticketOffice.getTicket()
//      audience.bag.minusAmount(ticket.fee)
//      ticketSeller.ticketOffice.plusAmount(ticket.fee)
//      audience.bag.ticket = ticket
//    }
//  }
//}