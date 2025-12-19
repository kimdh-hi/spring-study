package com.study.monolithic.product.infra

import com.study.monolithic.product.domian.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>
