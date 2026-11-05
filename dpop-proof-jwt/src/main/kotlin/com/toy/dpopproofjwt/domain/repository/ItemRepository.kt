package com.toy.dpopproofjwt.domain.repository

import com.toy.dpopproofjwt.domain.model.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long>
