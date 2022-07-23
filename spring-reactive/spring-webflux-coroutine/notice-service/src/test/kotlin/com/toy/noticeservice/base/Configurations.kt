package com.toy.noticeservice.base

import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories("com.toy.noticeservice.repository")
class RepositoryConfiguration