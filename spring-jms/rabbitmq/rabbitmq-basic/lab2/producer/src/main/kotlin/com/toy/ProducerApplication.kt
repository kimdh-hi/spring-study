package com.toy

import com.toy.domain.InvoiceCanceledMessage
import com.toy.domain.InvoiceCreatedMessage
import com.toy.domain.InvoicePaidMessage
import com.toy.producer.InvoiceProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalDate
import java.util.concurrent.ThreadLocalRandom

@SpringBootApplication
class ProducerApplication(
  private val producer: InvoiceProducer
): CommandLineRunner {
  override fun run(vararg args: String) {

    val invoiceNumber = "ivn-${ThreadLocalRandom.current().nextInt(1000, 2000)}"
    val invoiceCreatedMessage = InvoiceCreatedMessage(111.11, LocalDate.now().minusDays(1), "USD", invoiceNumber)
    producer.sendInvoiceCreated(invoiceCreatedMessage)

    val invoiceNumber2 = "ivn-${ThreadLocalRandom.current().nextInt(1000, 2000)}"
    val paymentNumber = "pay-${ThreadLocalRandom.current().nextInt(1000, 2000)}"
    val invoicePaidMessage = InvoicePaidMessage(invoiceNumber2, LocalDate.now(), paymentNumber)
    producer.sendInvoicePaid(invoicePaidMessage)

    val invoiceNumber3 = "ivn-${ThreadLocalRandom.current().nextInt(1000, 2000)}"
    val canceledMessage = InvoiceCanceledMessage(LocalDate.now(), invoiceNumber3, "umm...")
    producer.sendInvoiceCanceled(canceledMessage)
  }
}

fun main(args: Array<String>) {
  runApplication<ProducerApplication>(*args)
}
