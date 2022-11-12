package com.lecture.passbatch.repository

import com.lecture.passbatch.domain.Package
import org.springframework.data.jpa.repository.JpaRepository

interface PackageRepository: JpaRepository<Package, Int> {
}