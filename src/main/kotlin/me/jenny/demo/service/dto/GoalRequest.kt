package me.jenny.demo.service.dto

import me.jenny.demo.domain.DayOfWeekChoice
import java.time.LocalDate

data class GoalRequest(
    val title: String,
    val startDt: LocalDate,
    val totalGoal: Int,
    val unitGoal: Int,
    val attendDates: Set<DayOfWeekChoice>,
    val etc: String? = null,
)
