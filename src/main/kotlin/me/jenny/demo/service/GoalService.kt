package me.jenny.demo.service

import me.jenny.demo.domain.goal.GoalHistory
import me.jenny.demo.domain.goal.GoalHistoryRepository
import me.jenny.demo.domain.goal.GoalRepository
import me.jenny.demo.service.dto.GoalRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalService(
    private val goalRepository: GoalRepository,
    private val goalHistoryRepository: GoalHistoryRepository
) {
    @Transactional
    fun createGoal(goalRequest: GoalRequest) {
        val goal = goalRequest.toEntity()
        goalRepository.save(goal)

        val goalHistory = GoalHistory(goal.id, "목표 생성")
        goalHistoryRepository.save(goalHistory)
        goal.addHistory(goalHistory)
    }
}
