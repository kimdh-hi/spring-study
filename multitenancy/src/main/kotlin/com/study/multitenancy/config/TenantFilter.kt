package com.study.multitenancy.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Order(1)
class TenantFilter : OncePerRequestFilter() {

    companion object {
        const val TENANT_HEADER = "X-Tenant-Id"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val tenantId = request.getHeader(TENANT_HEADER)
            if (tenantId.isNullOrBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing $TENANT_HEADER header")
                return
            }
            TenantContext.setTenantId(tenantId)
            filterChain.doFilter(request, response)
        } finally {
            TenantContext.clear()
        }
    }
}
