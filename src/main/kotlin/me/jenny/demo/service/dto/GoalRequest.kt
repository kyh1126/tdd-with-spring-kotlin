package me.jenny.demo.service.dto

import me.jenny.demo.domain.DayOfWeekChoice
import me.jenny.demo.domain.goal.Goal
import java.time.LocalDate

data class GoalRequest(
    val title: String,
    val startDt: LocalDate,
    val totalGoal: Int,
    val unitGoal: Int,
    val attendDates: Set<DayOfWeekChoice>,
    val etc: String? = null,
) {
    fun toEntity(): Goal {
        return Goal(
            title,
            startDt,
            totalGoal,
            unitGoal,
            attendDates,
            etc
        )
    }
}
