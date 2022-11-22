package com.lecture.pharmacy.api.pharmacy.repository

import com.lecture.pharmacy.api.pharmacy.entity.Pharmacy
import org.springframework.data.jpa.repository.JpaRepository

interface PharmacyRepository: JpaRepository<Pharmacy, Long> {

}