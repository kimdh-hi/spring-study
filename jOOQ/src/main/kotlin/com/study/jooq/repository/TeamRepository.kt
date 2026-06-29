package com.study.jooq.repository

import com.study.jooq.domain.Team
import com.study.jooq.domain.TeamWithUsers
import com.study.jooq.domain.User
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class TeamRepository(
    private val dsl: DSLContext
) {

    fun findAll(): List<Team> {
        return dsl.select()
            .from("team")
            .fetch { record ->
                Team(
                    id = record.get("id", Long::class.java),
                    name = record.get("name", String::class.java),
                    description = record.get("description", String::class.java),
                    createdAt = record.get("created_at", LocalDateTime::class.java),
                    updatedAt = record.get("updated_at", LocalDateTime::class.java)
                )
            }
    }

    fun findById(id: Long): Team? {
        return dsl.select()
            .from("team")
            .where(DSL.field("id").eq(id))
            .fetchOne { record ->
                Team(
                    id = record.get("id", Long::class.java),
                    name = record.get("name", String::class.java),
                    description = record.get("description", String::class.java),
                    createdAt = record.get("created_at", LocalDateTime::class.java),
                    updatedAt = record.get("updated_at", LocalDateTime::class.java)
                )
            }
    }

    fun findByIdWithUsers(id: Long): TeamWithUsers? {
        val team = findById(id) ?: return null

        val users = dsl.select()
            .from("user")
            .where(DSL.field("team_id").eq(id))
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

        return TeamWithUsers(team, users)
    }

    fun findAllWithUsers(): List<TeamWithUsers> {
        val teams = findAll()

        val allUsers = dsl.select()
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
            .groupBy { it.teamId }

        return teams.map { team ->
            TeamWithUsers(
                team = team,
                users = allUsers[team.id] ?: emptyList()
            )
        }
    }

    fun save(team: Team): Team {
        dsl.insertInto(DSL.table("team"))
            .columns(
                DSL.field("name"),
                DSL.field("description")
            )
            .values(team.name, team.description)
            .execute()

        // H2와 MySQL 양쪽 호환
        val id = dsl.lastID().toLong()

        return team.copy(id = id)
    }

    fun update(id: Long, team: Team): Team? {
        val updated = dsl.update(DSL.table("team"))
            .set(DSL.field("name"), team.name)
            .set(DSL.field("description"), team.description)
            .where(DSL.field("id").eq(id))
            .execute()

        return if (updated > 0) {
            findById(id)
        } else {
            null
        }
    }

    fun deleteById(id: Long): Boolean {
        val deleted = dsl.deleteFrom(DSL.table("team"))
            .where(DSL.field("id").eq(id))
            .execute()

        return deleted > 0
    }

    fun count(): Long {
        return dsl.selectCount()
            .from("team")
            .fetchOne(0, Long::class.java) ?: 0L
    }
}
