package com.study.testcontainer.domain

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String>
