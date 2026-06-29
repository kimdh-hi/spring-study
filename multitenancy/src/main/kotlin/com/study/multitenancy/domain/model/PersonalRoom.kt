package com.study.multitenancy.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "personal_rooms")
class PersonalRoom(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_1")
    val user1: User,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_2")
    val user2: User,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
) : TenantAwareEntity()
