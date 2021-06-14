package me.jenny.demo.service

import me.jenny.demo.domain.DayOfWeekChoice
import me.jenny.demo.domain.goal.Goal
import me.jenny.demo.domain.goal.GoalRepository
import me.jenny.demo.service.dto.GoalRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestConstructor
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDate
import java.util.*

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(classes = [GoalService::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@EnableAutoConfiguration
internal class GoalServiceTest(private val goalService: GoalService) {
    @MockBean
    lateinit var goalRespository: GoalRepository

    @Test
    @DisplayName("목표 저장")
    fun `목표를 저장한다`() {
        // given
        val mockGoal = GoalRequest(
            title = "TDD 프로젝트 완성",
            startDt = LocalDate.of(2021, 6, 6),
            totalGoal = 100,
            unitGoal = 5,
            attendDates = setOf(
                DayOfWeekChoice.MONDAY,
                DayOfWeekChoice.TUESDAY,
                DayOfWeekChoice.WEDNESDAY,
                DayOfWeekChoice.THURSDAY,
                DayOfWeekChoice.FRIDAY
            )
        ).toEntity()

        given(goalRespository.save(any(Goal::class.java))).willReturn(mockGoal)
        given(goalRespository.findById(anyLong())).willReturn(Optional.of(mockGoal))

        // when
        goalService.saveGoal(mockGoal)

        // then
        val actualGoal = goalRespository.findByIdOrNull(mockGoal.id)

        verify(goalRespository, times(1)).save(mockGoal)
        assertEquals(mockGoal, actualGoal)
    }


    companion object {
        @Container
        private val mySqlContainer = MySQLContainer<Nothing>("mysql:latest")

        @JvmStatic
        @DynamicPropertySource
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { mySqlContainer.jdbcUrl }
            registry.add("spring.datasource.username") { mySqlContainer.username }
            registry.add("spring.datasource.password") { mySqlContainer.password }
            registry.add("spring.datasource.driver-class-name") { mySqlContainer.driverClassName }
        }
    }
}
