package com.study.datasync.order.order.domain

import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, String>
