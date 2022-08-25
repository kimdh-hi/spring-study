package com.toy.rabbitmqservice.domain

import com.toy.rabbitmqservice.config.NoArg

@NoArg
data class User (
    val id: Long,
    val name: String
)