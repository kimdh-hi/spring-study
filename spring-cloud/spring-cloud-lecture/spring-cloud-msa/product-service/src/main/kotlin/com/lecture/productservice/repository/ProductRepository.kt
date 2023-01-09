package com.lecture.productservice.repository

import com.lecture.productservice.domain.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository: JpaRepository<Product, String>