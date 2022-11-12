package com.lecture.passbatch.repository

import com.lecture.passbatch.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserReposiotry: JpaRepository<User, String> {
}