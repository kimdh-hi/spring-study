package com.study.multitenancy.domain.repository

import com.study.multitenancy.domain.model.PersonalRoom
import org.springframework.data.jpa.repository.JpaRepository

interface PersonalRoomRepository : JpaRepository<PersonalRoom, Long>
