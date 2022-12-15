package com.lecture.pharmacy.api.pharmacy.service

import com.lecture.pharmacy.api.pharmacy.repository.PharmacyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PharmacyService(
  private val pharmacyRepository: PharmacyRepository
) {

  @Transactional
  fun updateAddress(id: Long, address: String) {
    val pharmacy = pharmacyRepository.findByIdOrNull(id) ?: throw RuntimeException("pharmacy not found")
    pharmacy.changeAddress(address)
  }
}