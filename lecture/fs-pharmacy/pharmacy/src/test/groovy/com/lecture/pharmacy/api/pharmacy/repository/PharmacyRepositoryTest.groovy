package com.lecture.pharmacy.api.pharmacy.repository

import com.lecture.pharmacy.api.AbstractIntegrationContainerBaseTest
import com.lecture.pharmacy.api.pharmacy.entity.Pharmacy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import java.time.LocalDateTime

@SpringBootTest
class PharmacyRepositoryTest extends AbstractIntegrationContainerBaseTest {

  @Autowired
  private PharmacyRepository pharmacyRepository

  def setup() {
    pharmacyRepository.deleteAll()
  }

  def "save"() {
    given:
    String address = "서울시 동대문구"
    String name = "동대문 약국"
    double latitude = 11.11
    double longitude = 22.22

    def pharmacy = new Pharmacy()
    pharmacy.address = address
    pharmacy.name = name
    pharmacy.latitude = latitude
    pharmacy.longitude = longitude

    when:
    def savedPharmacy = pharmacyRepository.save(pharmacy)

    then:
    savedPharmacy.address == address
    savedPharmacy.name == name
    savedPharmacy.latitude == latitude
    savedPharmacy.longitude == longitude
  }

  def "saveAll"() {
    given:
    String address = "서울시 동대문구"
    String name = "동대문 약국"
    double latitude = 11.11
    double longitude = 22.22

    def pharmacy = new Pharmacy()
    pharmacy.address = address
    pharmacy.name = name
    pharmacy.latitude = latitude
    pharmacy.longitude = longitude

    when:
    pharmacyRepository.saveAll(Arrays.asList(pharmacy))
    def result = pharmacyRepository.findAll()

    then:
    result.size() == 1
  }

  def "baseEntity"() {
    given:
    def now = LocalDateTime.now()
    String address = "서울시 동대문구"
    String name = "동대문 약국"
    double latitude = 11.11
    double longitude = 22.22

    def pharmacy = new Pharmacy()
    pharmacy.address = address
    pharmacy.name = name
    pharmacy.latitude = latitude
    pharmacy.longitude = longitude

    when:
    def entity = pharmacyRepository.save(pharmacy)

    then:
    entity.createdDate != null
    entity.updatedDate != null
    entity.createdDate.isAfter(now)
    entity.updatedDate.isAfter(now)
  }
}