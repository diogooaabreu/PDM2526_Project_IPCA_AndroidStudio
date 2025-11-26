package ipca.example.habitslistapp.ui.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Habit(
    val id: String? = null,
    val nome: String = "",
    val descricao: String = "",
    val criadoPor: String = "",
    val partilhadoCom: List<String> = emptyList()
)

class HabitsViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _myHabits = MutableStateFlow<List<Habit>>(emptyList())
    val myHabits: StateFlow<List<Habit>> = _myHabits

    private val _sharedHabits = MutableStateFlow<List<Habit>>(emptyList())
    val sharedHabits: StateFlow<List<Habit>> = _sharedHabits

    private val userId = auth.currentUser?.uid // ID do user atualmente autenticado

    private val userEmail = auth.currentUser?.email // O email do utilizador com login é necessário para procurar hábitos partilhados

    init {
        loadHabits()
    }

    fun loadHabits() {
        if (userId == null || userEmail == null) return

        // 1. Carregar Hábitos Criados
        firestore.collection("habits")
            .whereEqualTo("criadoPor", userId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    _myHabits.value = snapshot.documents.mapNotNull { it.toObject(Habit::class.java)?.copy(id = it.id) }
                }
            }

        // 2. Carregar Hábitos Partilhados Comigo (onde o meu email está na lista partilhadoCom)
        firestore.collection("habits")
            .whereArrayContains("partilhadoCom", userEmail)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    _sharedHabits.value = snapshot.documents.mapNotNull { it.toObject(Habit::class.java)?.copy(id = it.id) }
                }
            }
    }

//Adiciona um novo hábito ao Firestore
    fun addHabit(nome: String, descricao: String) {
        if (userId == null) return

        val newHabit = Habit(
            nome = nome,
            descricao = descricao,
            criadoPor = userId

        )

        firestore.collection("habits").add(newHabit)
    }
//Elimina um hábito do Firestore
    fun deleteHabit(habitId: String) {
        firestore.collection("habits").document(habitId).delete()
    }
    //Atualiza um hábito do Firestore
    fun updateHabit(habitId: String, nome: String, descricao: String) {
        firestore.collection("habits").document(habitId)
            .update(mapOf("nome" to nome, "descricao" to descricao))
    }

//Partilha um hábito do Firestore
    fun shareHabit(habitId: String, email: String) {
        // Validação básica de e-mail e que o e-mail não é o meu
        if (email.isNotBlank() && email != userEmail) {
            firestore.collection("habits").document(habitId)
                // O arrayUnion adiciona o elemento ao array se ainda não existir
                .update("partilhadoCom", com.google.firebase.firestore.FieldValue.arrayUnion(email))
        }
    }
}