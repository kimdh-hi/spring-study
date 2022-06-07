package com.toy.springjavamail.common

import org.slf4j.LoggerFactory
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import javax.mail.internet.InternetAddress

//@Component
class MailSender (
    private val javaMailSender: JavaMailSender,
    private val from: InternetAddress,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun send(to: String, subject: String, text: String) {
        val simpleMailMessage = SimpleMailMessage()
        simpleMailMessage.setFrom(from.address)
        simpleMailMessage.setTo(to)
        simpleMailMessage.setSubject(subject)
        simpleMailMessage.setText(text)

        javaMailSender.send(simpleMailMessage)
    }
}