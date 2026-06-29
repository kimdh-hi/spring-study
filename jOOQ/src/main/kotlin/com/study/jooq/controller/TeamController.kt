package com.study.jooq.controller

import com.study.jooq.domain.Team
import com.study.jooq.domain.TeamWithUsers
import com.study.jooq.service.TeamService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/teams")
class TeamController(
    private val teamService: TeamService
) {

    @GetMapping
    fun getAllTeams(): List<Team> {
        return teamService.getAllTeams()
    }

    @GetMapping("/{id}")
    fun getTeam(@PathVariable id: Long): ResponseEntity<Team> {
        val team = teamService.getTeamById(id)
        return if (team != null) {
            ResponseEntity.ok(team)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{id}/with-users")
    fun getTeamWithUsers(@PathVariable id: Long): ResponseEntity<TeamWithUsers> {
        val teamWithUsers = teamService.getTeamWithUsers(id)
        return if (teamWithUsers != null) {
            ResponseEntity.ok(teamWithUsers)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/with-users")
    fun getAllTeamsWithUsers(): List<TeamWithUsers> {
        return teamService.getAllTeamsWithUsers()
    }

    @PostMapping
    fun createTeam(@RequestBody team: Team): ResponseEntity<Team> {
        val createdTeam = teamService.createTeam(team)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam)
    }

    @PutMapping("/{id}")
    fun updateTeam(@PathVariable id: Long, @RequestBody team: Team): ResponseEntity<Team> {
        val updatedTeam = teamService.updateTeam(id, team)
        return if (updatedTeam != null) {
            ResponseEntity.ok(updatedTeam)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTeam(@PathVariable id: Long): ResponseEntity<Void> {
        val deleted = teamService.deleteTeam(id)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
