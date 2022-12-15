package com.lecture.pharmacy.api.pharmacy.service

import com.lecture.pharmacy.api.AbstractIntegrationContainerBaseTest
import com.lecture.pharmacy.api.pharmacy.entity.Pharmacy
import com.lecture.pharmacy.api.pharmacy.repository.PharmacyRepository
import org.springframework.beans.factory.annotation.Autowired

class PharmacyServiceTest extends AbstractIntegrationContainerBaseTest {

  @Autowired
  private PharmacyService pharmacyService

  @Autowired
  private PharmacyRepository pharmacyRepository

  def setup() {
    pharmacyRepository.deleteAll()
  }

  def "updateAddress"() {
    given:
    String beforeAddress = "서울특별시 동대문구 장안동"
    String afterAddress = "서울특별시 동대문구 답십리1동"
    String name = "서울약국"

    def pharmacy = new Pharmacy()
    pharmacy.address = beforeAddress
    pharmacy.name = name

    when:
    def entity = pharmacyRepository.save(pharmacy)
    pharmacyService.updateAddress(entity.id, afterAddress)

    then:
    def result = pharmacyRepository.findById(entity.id)
    result.get().address == afterAddress
  }
}
