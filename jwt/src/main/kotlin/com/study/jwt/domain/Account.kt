package com.study.jwt.domain

import com.study.jwt.vo.BaseVO
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Account (

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    var id: String? = null,

    @Column(unique = true)
    var username: String,

    var password: String,

    @Column(name = "password_update_date")
    var passwordUpdateDate: LocalDateTime = LocalDateTime.now(),

    @JoinColumn(name = "role_id")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    var role: Role,
): BaseVO() {

    companion object {
        fun newInstance(username: String, password: String, role: Role): Account {
            return Account(
                username = username,
                password = password,
                role = role)
        }

        private const val serialVersionUID: Long = -5546758764761299602L
    }

    fun extendPasswordUpdateDate() {
        this.passwordUpdateDate = LocalDateTime.now().plusDays(30)
    }

    fun hasExpiredPasswordUpdateDate(): Boolean {
        return passwordUpdateDate.plusDays(90).isBefore(LocalDateTime.now())
    }
}