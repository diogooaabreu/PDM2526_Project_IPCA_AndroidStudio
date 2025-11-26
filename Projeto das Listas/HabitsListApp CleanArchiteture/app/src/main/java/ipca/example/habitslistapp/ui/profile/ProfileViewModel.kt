package ipca.example.habitslistapp.ui.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ipca.example.habitslistapp.ui.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileViewState(
    var id: String? = null,
    var nome: String? = null,
    var email: String? = null,
    var telemovel: String? = null,
    var isLoading: Boolean = false,
)

class ProfileViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    var uiState  = mutableStateOf(ProfileViewState())
        private set


    fun loadUserProfile() {
        uiState.value = uiState.value.copy(isLoading = true)

        val currentUser = auth.currentUser ?: return


        db.collection("users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userData = document.toObject(User::class.java)
                    uiState.value = uiState.value.copy(
                        id = document.id,
                        nome = userData?.nome,
                        email = userData?.email,
                        telemovel = userData?.telemovel,
                        isLoading = false
                    )

                }else{
                    uiState.value = uiState.value.copy(
                        isLoading = false
                    )
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                uiState.value = uiState.value.copy(
                    isLoading = false
                )
            }

    }

    fun logout() {
        auth.signOut()

    }
}
