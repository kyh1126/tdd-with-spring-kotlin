package me.jenny.demo.service

import me.jenny.demo.domain.goal.GoalHistory
import me.jenny.demo.domain.goal.GoalRepository
import me.jenny.demo.service.dto.GoalRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalService(
    private val goalRepository: GoalRepository,
) {
    @Transactional
    fun createGoal(goalRequest: GoalRequest) {
        val goal = goalRequest.toEntity()
        val goalHistory = GoalHistory(history = "목표 생성")

        goal.addHistory(goalHistory)
        goalRepository.save(goal)
    }
}
