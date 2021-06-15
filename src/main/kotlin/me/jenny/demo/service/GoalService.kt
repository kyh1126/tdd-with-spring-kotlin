package me.jenny.demo.service

import me.jenny.demo.domain.goal.GoalRepository
import me.jenny.demo.service.dto.GoalRequest
import org.springframework.stereotype.Service

@Service
class GoalService(private val goalRepository: GoalRepository) {
    fun createGoal(goalRequest: GoalRequest) = goalRepository.save(goalRequest.toEntity())
}
