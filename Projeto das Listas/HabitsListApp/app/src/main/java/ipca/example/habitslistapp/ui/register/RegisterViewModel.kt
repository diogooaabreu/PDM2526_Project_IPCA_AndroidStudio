package ipca.example.habitslistapp.ui.register

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


private const val TAG = "RegisterViewModel"


data class RegisterUiState(
    val nome: String? = null,
    val telemovel: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)


data class User(
    val nome: String = "",
    val telemovel: String = "",
    val email: String = ""

)

class RegisterViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    // Instância do Firebase Authentication
    private var auth: FirebaseAuth = Firebase.auth

    // Estado observável do ecrã
    val uiState = mutableStateOf(RegisterUiState())

    // Atualiza o nome
    fun updateName(newName: String) {
        uiState.value = uiState.value.copy(nome = newName)
    }

    //Atualiza o telemóvel
    fun updatePhone(newPhone: String) {
        uiState.value = uiState.value.copy(telemovel = newPhone)
    }

    // Atualiza o email quando o utilizador escreve
    fun updateEmail(newEmail: String) {
        uiState.value = uiState.value.copy(email = newEmail)
    }

    // Atualiza a password
    fun updatePassword(newPassword: String) {
        uiState.value = uiState.value.copy(password = newPassword)
    }

    // Atualiza a confirmação da password
    fun updateConfirmPassword(newPassword: String) {
        uiState.value = uiState.value.copy(confirmPassword = newPassword)
    }


// Função para adicionar utilizador ao Firestore

    private fun addUser(userId: String, nome: String, telemovel: String, email: String) {
        // Usa a nova classe de modelo (User)
        val newUser = User(
            nome = nome,
            telemovel = telemovel,
            email = email
        )

        firestore.collection("users")
            .document(userId) // Usa o UID do Auth como ID do documento do Firestore
            .set(newUser)
            .addOnSuccessListener {
                Log.d(TAG, "Utilizador adicionado com sucesso ao Firestore")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Erro ao adicionar utilizador ao Firestore", e)
            }
    }


    // Função principal que cria o utilizador no Firebase

    fun register(onRegisterSuccess: () -> Unit) {
        val current = uiState.value

        // Validação de todos os campos (melhorada)
        if (current.nome.isNullOrEmpty() || current.telemovel.isNullOrEmpty() || current.email.isNullOrEmpty() || current.password.isNullOrEmpty() || current.confirmPassword.isNullOrEmpty()) {
            uiState.value = current.copy(error = "Preencha todos os campos.", isLoading = false)
            return
        }

        // Verifica se as passwords coincidem
        if (current.password != current.confirmPassword) {
            uiState.value = current.copy(error = "As palavras-passe não coincidem.", isLoading = false)
            return
        }

        // Atualiza estado para mostrar o indicador de carregamento
        uiState.value = current.copy(isLoading = true, error = null)

        // Chama o Firebase Authentication para criar conta
        auth.createUserWithEmailAndPassword(current.email!!, current.password!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registo bem-sucedido
                    Log.d(TAG, "createUserWithEmail:success")

                    // *** CHAVE: Obter o UID do utilizador recém-criado e chamar addUser ***
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        addUser(
                            userId = firebaseUser.uid,
                            nome = current.nome!!,
                            telemovel = current.telemovel!!,
                            email = current.email!!
                        )
                    }

                    uiState.value = uiState.value.copy(isLoading = false, error = null)
                    onRegisterSuccess() // Volta ao login ou vai para home
                } else {
                    // Erro ao registar
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        error = "Falha ao registar. Email inválido ou a password é muito fraca (mínimo 6 carateres)."
                    )
                }
            }
    }
}
