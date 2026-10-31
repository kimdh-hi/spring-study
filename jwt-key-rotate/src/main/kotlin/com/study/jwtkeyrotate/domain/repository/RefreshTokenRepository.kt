package com.study.jwtkeyrotate.domain.repository

import com.study.jwtkeyrotate.domain.model.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshToken, String>
