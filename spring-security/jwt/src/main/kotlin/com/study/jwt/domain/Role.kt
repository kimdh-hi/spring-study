package com.study.jwt.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
class Role (

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "role_id")
    var id: String? = null,

    var name: String? = null,

    @JoinTable(
        name = "role_authority",
        joinColumns = [JoinColumn(name = "role_id")])
    @ElementCollection(targetClass = Authority::class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    val authorities: Set<Authority> = mutableSetOf()
) {

    fun getAuthority(): Authority {
        val admin = authorities.firstOrNull {it == Authority.ADMIN}

        return admin ?: Authority.USER
    }
}