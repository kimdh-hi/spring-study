package com.toy

import com.toy.domain.InvoiceCreatedMessage
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

    for (i in 0 until 200) {
      val invoiceNumber = "inv-${i%60}"
      val createdMessage = InvoiceCreatedMessage(ThreadLocalRandom.current().nextInt(200), LocalDate.now(), "USD", invoiceNumber)
      producer.sendInvoiceCreated(createdMessage)
    }
  }
}

fun main(args: Array<String>) {
  runApplication<ProducerApplication>(*args)
}
