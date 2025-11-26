package ipca.example.habitslistapp.ui.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

data class LoginState (
    var email : String? = null,
    var password : String? = null,
    var error : String? = null,
    var isLoading : Boolean = false
)

class LoginViewModel : ViewModel() {

    var uiState = mutableStateOf(LoginState())
        private set

    // Atualiza o email quando o utilizador escreve
    fun updateEmail(email : String) {
        uiState.value = uiState.value.copy(email = email)
    }

    fun updatePassword(password : String) {
        uiState.value = uiState.value.copy(password = password)
    }

    // Função principal para iniciar o processo de login. Recebe uma lambda a executar em caso de sucesso.
    fun login(onLoginSuccess:()->Unit) {

        uiState.value = uiState.value.copy(isLoading = true)

        if (uiState.value.email.isNullOrEmpty()) { //Verifica se o campo de email está vazio ou nulo.
            uiState.value = uiState.value.copy(
                isLoading = false,
                error = "Email is required")
            return
        }

        if (uiState.value.password.isNullOrEmpty()) { //Verifica se o campo de password está vazio ou nulo.
            uiState.value = uiState.value.copy(
                isLoading = false,
                error = "Password is required")
            return
        }

        var auth: FirebaseAuth
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(
            // Chama a API do Firebase para iniciar a autenticação.
            uiState.value.email!!,
            uiState.value.password!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    // Regista o sucesso do login.
                    val user = auth.currentUser
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        // Desativa o indicador de carregamento,
                        error = null)
                    // e limpa qualquer erro anterior.
                    onLoginSuccess()
                    // Executa a lambda passada, que geralmente navega o utilizador para a homepage.
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        error = "Wrong password or no internet connection")
                    // e define uma mensagem de erro genérica para o utilizador.
                }
            }
    }
}