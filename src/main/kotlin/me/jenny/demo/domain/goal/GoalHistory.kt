package me.jenny.demo.domain.goal

import com.fasterxml.jackson.annotation.JsonIgnore
import me.jenny.demo.domain.base.AuditableEntity
import me.jenny.demo.util.Stem
import javax.persistence.*

@Entity
@Table(name = "goal_history")
class GoalHistory(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    @JsonIgnore
    var goal: Goal? = null,

    var history: String

) : AuditableEntity() {
    @Transient
    private val stem = Stem(this, { id })
}
