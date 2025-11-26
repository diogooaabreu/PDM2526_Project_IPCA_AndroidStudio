package ipca.example.habitslistapp.ui.models 

/**
 * Modelo de dados para o utilizador.
 * Deve corresponder à estrutura guardada na coleção "users" no Firestore.
 */
data class User(
    val nome: String? = null,
    val email: String? = null,
    val tipo: String? = null,
    val telemovel: String? = null
)