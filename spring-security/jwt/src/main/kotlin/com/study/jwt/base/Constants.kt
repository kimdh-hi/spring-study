package com.study.jwt.base

object SecurityConstants {

    const val JWT_FILTER_PROCESSING_URL_PREFIX = "/api/**"
    val JWT_FILTER_SKIP_URL = listOf(
        "/accounts/signup", "/accounts/login", "/api/public"
    )

    const val BEARER_TYPE_PREFIX = "bearer "
}