package ipca.example.habitslistapp.ui.models

// Definição ÚNICA da data class
data class Habit(
    val id: String? = null,
    val nome: String = "",
    val descricao: String = "",
    val criadoPor: String = "",
    val partilhadoCom: List<String> = emptyList() // Incluindo o campo de partilha
)