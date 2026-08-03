package com.study.springgraalvm.infra

import com.study.springgraalvm.domain.model.Company
import org.springframework.data.jpa.repository.JpaRepository

interface CompanyJpaRepository : JpaRepository<Company, Long>
