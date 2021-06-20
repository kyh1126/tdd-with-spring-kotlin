package me.jenny.demo.service

import me.jenny.demo.domain.DayOfWeekChoice.*
import me.jenny.demo.domain.Progress
import me.jenny.demo.domain.goal.Goal
import me.jenny.demo.domain.goal.GoalHistoryRepository
import me.jenny.demo.domain.goal.GoalRepository
import me.jenny.demo.service.dto.GoalRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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

/**
 * Service layer test
 *
 * #1. 서비스 레이어는 데이터의 CRUD 를 Repository 에 위임하고 트랜잭션을 관리하는 것이 주요 책임이다.
 * - JPA 를 사용한다면 DTO <-> Entity 간 변환도 서비스 레이어에서 처리하는 것을 선호한다. 이유는 이 변환을 컨트롤러 레이어에서 수행한다면 Lazy 조회 시 LazyInitializationException 이 발생할 위험에 노출되기 때문이다.
 * - 물론 이 경우에도 실제 DTO <-> Entity 간 변환 로직 자체는 DTO 에 담고, 서비스 레이어에서는 DTO 의 변환 로직을 호출할 뿐이다.
 *
 * #2. Repository 의 동작은 위의 Repository 테스트에서 이미 확인했으므로, 서비스 레이어에서는 Repository 메서드가 제대로 호출되는지만 확인하면 되므로 실제 Repository 대신 Mock Repository 를 사용하면 된다. 따라서 Mockito 필요
 * - 필요하다면 @SpringBootTest(classes = {A.class, B.class, ...})도 사용 가능하다.
 *
 * #3. Mock 을 사용하므로 실제 저장/조회가 발생하지 않는다.
 * - 생성, 삭제, 조회 시에는 생성/삭제/조회를 호출하는 메서드 호출 여부를 verify 하면 된다.
 * - 수정 시에는 명시적으로 save()나 saveAndFlush()가 호출되지 않을 수도 있으므로 verify 로는 테스트가 불가능하므로 Mock 에 사용되는 엔티티나 DTO 의 상태 변경을 assert 한다.
 * - Mock 에 사용되는 엔티티나 DTO 를 생성하는 부분이 매우 번거로울 수 있다.
 */
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(classes = [GoalService::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@EnableAutoConfiguration
internal class GoalServiceTest(private val goalService: GoalService) {
    @MockBean
    lateinit var goalRespository: GoalRepository

    private val weekdays = setOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY)

    @Test
    @DisplayName("목표 저장")
    fun `목표를 저장한다`() {
        // given
        val goalRequest = GoalRequest(
            title = "TDD 프로젝트 완성",
            startDt = LocalDate.now().minusDays(1),
            totalGoal = 100,
            unitGoal = 5,
            attendDates = weekdays
        )
        val mockGoal = goalRequest.toEntity()

        given(goalRespository.save(any(Goal::class.java))).willReturn(mockGoal)
        given(goalRespository.findById(anyLong())).willReturn(Optional.of(mockGoal))

        // when
        goalService.createGoal(goalRequest)

        // then
        val actualGoal = goalRespository.findByIdOrNull(mockGoal.id)

        assertNotNull(actualGoal)
        actualGoal!!

        verify(goalRespository, times(1)).save(mockGoal)
        assertEquals(mockGoal, actualGoal)
        assertEquals(actualGoal.status, Progress.IN_PROGRESS)
        assertEquals(actualGoal.attendDates, weekdays)
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
