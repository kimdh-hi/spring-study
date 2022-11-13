package com.lecture.passbatch.repository

import com.lecture.passbatch.domain.Package
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.persistence.EntityManager

@SpringBootTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
class PackageRepositoryTest(
  private val packageRepository: PackageRepository,
  private val entityManager: EntityManager
) {

  @Test
  fun save() {
    //given
    val packaze = Package(packageName = "name", period = 10)

    //when
    val savedPackage = packageRepository.save(packaze)

    //when
    assertNotNull(savedPackage.packageSeq)
  }

  @Test
  fun `findByCreatedAtAfter`() {
    //given
    val dateTime = LocalDateTime.now().minusMinutes(1)
    val package1 = Package(packageName = "name1", period = 10)
    packageRepository.save(package1)
    val package2 = Package(packageName = "name2", period = 10)
    packageRepository.save(package2)

    //when
    val packages = packageRepository.findByCreatedAtAfter(dateTime, PageRequest.of(0, 1, Sort.by("packageSeq").descending()))

    //then
    assertAll({
      assertEquals(1, packages.size)
      assertEquals("name2", package2.packageName)
    })
  }

  @Test
  fun updateCountAndPeriod() {
    //given
    val packaze = Package(packageName = "name", period = 10)
    val savedPackage = packageRepository.save(packaze)
    entityManager.flush()
    entityManager.clear()
    //when
    val updatedCount = packageRepository.updateCountAndPeriod(savedPackage.packageSeq!!, 30, 120)
    val updatedPackage = packageRepository.findByIdOrNull(savedPackage.packageSeq)!!

    //then
    assertAll({
      assertEquals(1, updatedCount)
      assertEquals(30, updatedPackage.count)
      assertEquals(120, updatedPackage.period)
    })
  }

  @Test
  fun delete() {
    //given
    val packaze = Package(packageName = "name", period = 10)
    val savedPackage = packageRepository.save(packaze)

    //when
    packageRepository.deleteById(savedPackage.packageSeq!!)

    //then
    assertNull(packageRepository.findByIdOrNull(savedPackage.packageSeq!!))
  }
}