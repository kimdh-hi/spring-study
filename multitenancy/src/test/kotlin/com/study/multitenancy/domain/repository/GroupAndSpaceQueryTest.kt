package com.study.multitenancy.domain.repository

import com.study.multitenancy.config.TenantContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GroupAndSpaceQueryTest(
    @Autowired private val groupRepository: GroupRepository,
    @Autowired private val spaceRepository: SpaceRepository,
) {

    @AfterEach
    fun tearDown() {
        TenantContext.clear()
    }

    @Test
    fun `tenantA 그룹 목록 조회`() {
        TenantContext.setTenantId("tenantA")

        val groups = groupRepository.findAll()
        println("=== TenantA Groups ===")
        groups.forEach { println("Group(id=${it.id}, name=${it.name})") }
    }

    @Test
    fun `tenantB 그룹 목록 조회`() {
        TenantContext.setTenantId("tenantB")

        val groups = groupRepository.findAll()
        println("=== TenantB Groups ===")
        groups.forEach { println("Group(id=${it.id}, name=${it.name})") }
    }

    @Test
    fun `tenantA 스페이스 목록 조회`() {
        TenantContext.setTenantId("tenantA")

        val spaces = spaceRepository.findAll()
        println("=== TenantA Spaces ===")
        spaces.forEach { println("Space(id=${it.id}, name=${it.name}, groupId=${it.group.id})") }
    }

    @Test
    fun `tenantB 스페이스 목록 조회`() {
        TenantContext.setTenantId("tenantB")

        val spaces = spaceRepository.findAll()
        println("=== TenantB Spaces ===")
        spaces.forEach { println("Space(id=${it.id}, name=${it.name}, groupId=${it.group.id})") }
    }
}
