package me.jenny.demo.domain.goal

import org.springframework.data.jpa.repository.JpaRepository

interface GoalHistoryRepository : JpaRepository<GoalHistory, Long>
