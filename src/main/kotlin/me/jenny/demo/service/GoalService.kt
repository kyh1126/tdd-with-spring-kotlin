package me.jenny.demo.service

import me.jenny.demo.domain.goal.Goal
import me.jenny.demo.domain.goal.GoalRepository
import org.springframework.stereotype.Service

@Service
class GoalService(
    private val goalRepository: GoalRepository
) {
    fun saveGoal(goal: Goal) {
        goalRepository.save(goal)
    }
}
