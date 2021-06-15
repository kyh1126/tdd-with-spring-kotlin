package me.jenny.demo.util

import me.jenny.demo.domain.DayOfWeekChoice
import me.jenny.demo.domain.goal.Goal
import me.jenny.demo.domain.goal.GoalRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class StemTest(private val goalRespository: GoalRepository) {

    @Test
    @DisplayName("동일 id equals, hashCode 테스트")
    fun `엔티티 id가 같으면 equals, hashCode 는 같다`() {
        // given
        val goal1 = goal()
        val goal2 = Goal(
            title = "TDD 프로젝트 완성2",
            startDt = LocalDate.now(),
            totalGoal = 1,
            unitGoal = 1,
            attendDates = setOf(DayOfWeekChoice.NONE)
        )

        // then
        assertEquals(goal1.id, goal2.id)
        assertEquals(goal1.hashCode(), goal2.hashCode())
        assertTrue(goal1 == goal2)
    }

    @Test
    @DisplayName("다른 id equals, hashCode 테스트")
    @Transactional
    fun `엔티티 id가 다르면 equals, hashCode 는 다르다`() {
        // given
        val goal1 = goal()
        val goal2 = goal()

        // when
        goalRespository.save(goal1)
        goalRespository.save(goal2)

        // then
        val goals = goalRespository.findAll()
        assertEquals(goals.size, 2)

        assertNotEquals(goal1.id, goal2.id)
        assertNotEquals(goal1.hashCode(), goal2.hashCode())
        assertFalse(goal1 == goal2)
    }

    private fun goal() = Goal(
        title = "TDD 프로젝트 완성",
        startDt = LocalDate.of(2021, 6, 6),
        totalGoal = 100,
        unitGoal = 5,
        attendDates = setOf(DayOfWeekChoice.MONDAY)
    )
}
