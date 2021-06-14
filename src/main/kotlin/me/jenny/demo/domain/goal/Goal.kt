package me.jenny.demo.domain.goal

import me.jenny.demo.domain.DayOfWeekChoice
import me.jenny.demo.domain.base.AuditableEntity
import me.jenny.demo.util.Stem
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "goal")
class Goal(
    var title: String,

    var startDt: LocalDate,

    var totalGoal: Int,

    var unitGoal: Int,

    var attendDates: Set<DayOfWeekChoice>,

    var etc: String?
) : AuditableEntity() {
    private val stem = Stem(this, { id })

    override fun equals(other: Any?) = stem.eq(other)
    override fun hashCode() = stem.hc()
}