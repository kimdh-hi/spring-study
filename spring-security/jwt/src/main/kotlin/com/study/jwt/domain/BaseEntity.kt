package com.study.jwt.domain

import java.io.Serializable

abstract class BaseEntity: Serializable {
    companion object {
        private const val serialVersionUID: Long = 656073036783298509L
    }
}