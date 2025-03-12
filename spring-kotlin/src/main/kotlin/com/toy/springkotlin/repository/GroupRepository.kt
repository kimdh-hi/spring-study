package com.toy.springkotlin.repository

import com.toy.springkotlin.entity.Group
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository : JpaRepository<Group, String>
