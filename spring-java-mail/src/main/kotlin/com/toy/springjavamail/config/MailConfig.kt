package com.toy.springjavamail.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*
import javax.mail.internet.InternetAddress

@Configuration
class MailConfig(
    private val env: Environment
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun amazonSimpleEmailService(): AmazonSimpleEmailService {
        val accessKey = env.getProperty("aws-secret.access-key")
        val secretKey = env.getProperty("aws-secret.secret-key")
        return AmazonSimpleEmailServiceClientBuilder.standard()
            .withCredentials(
                AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey))
            )
            .withRegion(Regions.AP_NORTHEAST_2)
            .build()
    }

//    @Bean
//    fun javaMailSender(): JavaMailSenderImpl {
//        val mailSender= JavaMailSenderImpl()
//
//        mailSender.host = env.getProperty("spring.mail.host")
//        mailSender.port = env.getProperty("spring.mail.port")?.toInt() ?: 587
//        mailSender.username = env.getProperty("spring.mail.username")
//        mailSender.password = env.getProperty("spring.mail.password")
//        mailSender.defaultEncoding = env.getProperty("spring.mail.properties.mime.charset")
//        mailSender.javaMailProperties = getJavaMailProperties()
//        return mailSender
//    }
//
//    private fun getJavaMailProperties(): Properties {
//        val properties = Properties()
//        properties.setProperty("mail.transport.protocol", env.getProperty("spring.mail.properties.transport.protocol"))
//        properties.setProperty("mail.smtp.port", env.getProperty("spring.mail.port"))
//        properties.setProperty("mail.smtp.auth", env.getProperty("spring.mail.properties.smtp.auth"))
//        properties.setProperty("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.smtp.starttls.enable"))
//        properties.setProperty("mail.smtp.ssl.trust", env.getProperty("spring.mail.host"))
//        //properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2")
//        return properties
//    }
//
//    @Bean
//    fun fromAddress(): InternetAddress {
//        val address = InternetAddress()
//        address.personal = "test-personal"
//        address.address = env.getProperty("spring.mail.username")
//        return address
//    }
}