package com.study.jooq.repository

import com.study.jooq.domain.User
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class UserRepository(
    private val dsl: DSLContext
) {

    fun findAll(): List<User> {
        return dsl.select()
            .from("user")
            .fetch { record ->
                User(
                    id = record.get("id", Long::class.java),
                    teamId = record.get("team_id", Long::class.java),
                    username = record.get("username", String::class.java),
                    email = record.get("email", String::class.java),
                    fullName = record.get("full_name", String::class.java),
                    createdAt = record.get("created_at", LocalDateTime::class.java),
                    updatedAt = record.get("updated_at", LocalDateTime::class.java)
                )
            }
    }

    fun findById(id: Long): User? {
        return dsl.select()
            .from("user")
            .where(DSL.field("id").eq(id))
            .fetchOne { record ->
                User(
                    id = record.get("id", Long::class.java),
                    teamId = record.get("team_id", Long::class.java),
                    username = record.get("username", String::class.java),
                    email = record.get("email", String::class.java),
                    fullName = record.get("full_name", String::class.java),
                    createdAt = record.get("created_at", LocalDateTime::class.java),
                    updatedAt = record.get("updated_at", LocalDateTime::class.java)
                )
            }
    }

    fun findByTeamId(teamId: Long): List<User> {
        return dsl.select()
            .from("user")
            .where(DSL.field("team_id").eq(teamId))
            .fetch { record ->
                User(
                    id = record.get("id", Long::class.java),
                    teamId = record.get("team_id", Long::class.java),
                    username = record.get("username", String::class.java),
                    email = record.get("email", String::class.java),
                    fullName = record.get("full_name", String::class.java),
                    createdAt = record.get("created_at", LocalDateTime::class.java),
                    updatedAt = record.get("updated_at", LocalDateTime::class.java)
                )
            }
    }

    fun findByUsername(username: String): User? {
        return dsl.select()
            .from("user")
            .where(DSL.field("username").eq(username))
            .fetchOne { record ->
                User(
                    id = record.get("id", Long::class.java),
                    teamId = record.get("team_id", Long::class.java),
                    username = record.get("username", String::class.java),
                    email = record.get("email", String::class.java),
                    fullName = record.get("full_name", String::class.java),
                    createdAt = record.get("created_at", LocalDateTime::class.java),
                    updatedAt = record.get("updated_at", LocalDateTime::class.java)
                )
            }
    }

    fun findByEmail(email: String): User? {
        return dsl.select()
            .from("user")
            .where(DSL.field("email").eq(email))
            .fetchOne { record ->
                User(
                    id = record.get("id", Long::class.java),
                    teamId = record.get("team_id", Long::class.java),
                    username = record.get("username", String::class.java),
                    email = record.get("email", String::class.java),
                    fullName = record.get("full_name", String::class.java),
                    createdAt = record.get("created_at", LocalDateTime::class.java),
                    updatedAt = record.get("updated_at", LocalDateTime::class.java)
                )
            }
    }

    fun save(user: User): User {
        dsl.insertInto(DSL.table("user"))
            .columns(
                DSL.field("team_id"),
                DSL.field("username"),
                DSL.field("email"),
                DSL.field("full_name")
            )
            .values(user.teamId, user.username, user.email, user.fullName)
            .execute()

        // H2와 MySQL 양쪽 호환
        val id = dsl.lastID().toLong()

        return user.copy(id = id)
    }

    fun update(id: Long, user: User): User? {
        val updated = dsl.update(DSL.table("user"))
            .set(DSL.field("team_id"), user.teamId)
            .set(DSL.field("username"), user.username)
            .set(DSL.field("email"), user.email)
            .set(DSL.field("full_name"), user.fullName)
            .where(DSL.field("id").eq(id))
            .execute()

        return if (updated > 0) {
            findById(id)
        } else {
            null
        }
    }

    fun deleteById(id: Long): Boolean {
        val deleted = dsl.deleteFrom(DSL.table("user"))
            .where(DSL.field("id").eq(id))
            .execute()

        return deleted > 0
    }

    fun deleteByTeamId(teamId: Long): Int {
        return dsl.deleteFrom(DSL.table("user"))
            .where(DSL.field("team_id").eq(teamId))
            .execute()
    }

    fun count(): Long {
        return dsl.selectCount()
            .from("user")
            .fetchOne(0, Long::class.java) ?: 0L
    }

    fun countByTeamId(teamId: Long): Long {
        return dsl.selectCount()
            .from("user")
            .where(DSL.field("team_id").eq(teamId))
            .fetchOne(0, Long::class.java) ?: 0L
    }
}
