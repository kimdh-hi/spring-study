package com.study.jooq.domain

data class TeamWithUsers(
    val team: Team,
    val users: List<User>
)
