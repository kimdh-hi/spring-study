package com.study.multitenancy.config

object TenantContext {
    private val currentTenant = ThreadLocal<String?>()

    fun setTenantId(tenantId: String?) {
        currentTenant.set(tenantId)
    }

    fun getTenantId(): String? = currentTenant.get()

    fun clear() {
        currentTenant.remove()
    }
}
