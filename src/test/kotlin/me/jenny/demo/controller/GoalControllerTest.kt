package me.jenny.demo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.jenny.demo.domain.DayOfWeekChoice
import me.jenny.demo.domain.goal.GoalRepository
import me.jenny.demo.service.dto.GoalRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
internal class GoalControllerTest(
    private val goalRespository: GoalRepository,
    private val objectMapper: ObjectMapper,
    private val mockMvc: MockMvc
) {

    @Test
    @Transactional
    @DisplayName("목표 저장 api call")
    fun `목표 저장 api 실행한다`() {
        // given
        val dto = GoalRequest(
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
        )

        // when
        mockMvc.post("/goals") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(dto)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json("{}") }
        }

        // then
        val savedGoal = goalRespository.findByTitle(dto.title) ?: throw NoSuchElementException()
        // TODO: Customizing exception
        // { GoalNotFoundException() }
        assertNotNull(savedGoal)
        assertEquals(dto.title, savedGoal.title)
        assertEquals(dto.attendDates, savedGoal.attendDates)
        assertEquals(dto.etc, savedGoal.etc)
    }

//    @Test
//    fun `test`() {
//        mockMvc.get("/goals") {
//            accept = MediaType.APPLICATION_JSON
//        }.andExpect {
//            content { contentType(MediaType.APPLICATION_JSON) }
//            jsonPath("$[0].name") { value("jin") }
//            jsonPath("$[1].name") { value("yun") }
//            jsonPath("$[2].name") { value("wan") }
//            jsonPath("$[3].name") { value("kong") }
//            jsonPath("$[4].name") { value("joo") }
//        }.andDo {
//            print()
//        }
//    }
}
