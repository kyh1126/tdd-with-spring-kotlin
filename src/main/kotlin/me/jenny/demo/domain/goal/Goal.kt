package me.jenny.demo.domain.goal

import me.jenny.demo.domain.DayOfWeekChoice
import me.jenny.demo.domain.DayOfWeekChoiceSetConverter
import me.jenny.demo.domain.Progress
import me.jenny.demo.domain.base.AuditableEntity
import me.jenny.demo.util.Stem
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "goal")
class Goal(
    var title: String,

    var startDt: LocalDate,

    var totalGoal: Int,

    var unitGoal: Int,

    @Convert(converter = DayOfWeekChoiceSetConverter::class)
    var attendDates: Set<DayOfWeekChoice>,

    var etc: String? = null,

    @OneToMany(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "goal_id")
    val histories: MutableList<GoalHistory> = mutableListOf()

) : AuditableEntity() {
    @Transient
    private val stem = Stem(this, { id })

    var status: Progress? = null
        get() = when {
            field in setOf(Progress.CANCELLED, Progress.ON_HOLD, Progress.COMPLETED) -> field
            (startDt.isAfter(LocalDate.now())) -> Progress.WAITING
            else -> Progress.IN_PROGRESS
        }

    override fun equals(other: Any?) = stem.eq(other)
    override fun hashCode() = stem.hc()

    fun addHistory(history: GoalHistory) = histories.add(history)
}
