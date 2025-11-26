package ipca.example.habitslistapp.ui.homepage

import ipca.example.habitslistapp.ui.models.Habit

data class HabitsViewState(
    val myHabits: List<Habit> = emptyList(),
    val sharedHabits: List<Habit> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
