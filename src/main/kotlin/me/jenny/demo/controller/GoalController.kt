package me.jenny.demo.controller

import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import me.jenny.demo.service.GoalService
import me.jenny.demo.service.dto.GoalRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["Goal API"])
class GoalController(private val goalService: GoalService) {

    @Operation(summary = "목표 저장")
    @PostMapping("/goals")
    fun createGoal(@RequestBody goalRequest: GoalRequest) = goalService.createGoal(goalRequest)

}
