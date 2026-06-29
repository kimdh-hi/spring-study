package com.study.multitenancy.config

import org.hibernate.context.spi.CurrentTenantIdentifierResolver
import org.springframework.stereotype.Component

@Component
class TenantIdentifierResolver : CurrentTenantIdentifierResolver<String> {

    override fun resolveCurrentTenantIdentifier(): String {
        return TenantContext.getTenantId() ?: DEFAULT_TENANT
    }

    override fun validateExistingCurrentSessions(): Boolean = true

    companion object {
        const val DEFAULT_TENANT = "default"
    }
}
