package me.jenny.demo.domain.goal

import me.jenny.demo.domain.base.AuditableEntity
import me.jenny.demo.util.Stem
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Transient

@Entity
@Table(name = "goal_history")
class GoalHistory(
    @Column(name = "goal_id")
    var goalId: Long,

    var history: String

) : AuditableEntity() {
    @Transient
    private val stem = Stem(this, { id })
}
