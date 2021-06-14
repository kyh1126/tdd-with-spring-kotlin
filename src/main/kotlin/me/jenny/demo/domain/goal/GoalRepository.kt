package me.jenny.demo.domain.goal

import org.springframework.data.jpa.repository.JpaRepository

interface GoalRepository : JpaRepository<Goal, Long> {
    fun findByTitle(title: String): Goal?
}
