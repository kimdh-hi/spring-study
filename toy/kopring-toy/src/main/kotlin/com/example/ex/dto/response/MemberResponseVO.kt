package com.example.ex.dto.response

import com.querydsl.core.annotations.QueryProjection

data class MemberResponseVO @QueryProjection constructor(
    val id: Long,
    val username: String
)