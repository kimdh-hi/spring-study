package com.study.jooq.service

import com.study.jooq.domain.Team
import com.study.jooq.domain.TeamWithUsers
import com.study.jooq.repository.TeamRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TeamService(
    private val teamRepository: TeamRepository
) {

    fun getAllTeams(): List<Team> {
        return teamRepository.findAll()
    }

    fun getTeamById(id: Long): Team? {
        return teamRepository.findById(id)
    }

    fun getTeamWithUsers(id: Long): TeamWithUsers? {
        return teamRepository.findByIdWithUsers(id)
    }

    fun getAllTeamsWithUsers(): List<TeamWithUsers> {
        return teamRepository.findAllWithUsers()
    }

    @Transactional
    fun createTeam(team: Team): Team {
        return teamRepository.save(team)
    }

    @Transactional
    fun updateTeam(id: Long, team: Team): Team? {
        return teamRepository.update(id, team)
    }

    @Transactional
    fun deleteTeam(id: Long): Boolean {
        return teamRepository.deleteById(id)
    }
}
