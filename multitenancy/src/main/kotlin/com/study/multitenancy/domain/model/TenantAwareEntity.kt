package com.study.multitenancy.domain.model

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.TenantId

@MappedSuperclass
abstract class TenantAwareEntity(
    @TenantId
    @Column(name = "tenant_id", nullable = false, updatable = false)
    val tenantId: String? = null,
)
