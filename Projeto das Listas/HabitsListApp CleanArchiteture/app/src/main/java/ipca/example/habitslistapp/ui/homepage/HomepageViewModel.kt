package ipca.example.habitslistapp.ui.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel

import  ipca.example.habitslistapp.ui.repository.HabitRepository
import ipca.example.habitslistapp.ui.models.Habit

import ipca.example.habitslistapp.ui.repository.ResultWrapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {

    var uiState = androidx.compose.runtime.mutableStateOf(HabitsViewState())
        private set

    // -----------------------------
    // 1) Carregar hábitos do utilizador
    // -----------------------------
    fun loadMyHabits() {
        habitRepository.fetchMyHabits().onEach { result ->
            when(result) {
                is ResultWrapper.Loading -> {
                    uiState.value = uiState.value.copy(isLoading = true)
                }
                is ResultWrapper.Success -> {
                    uiState.value = uiState.value.copy(
                        myHabits = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is ResultWrapper.Error -> {
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    // -----------------------------
    // 2) Carregar hábitos partilhados comigo
    // -----------------------------
    fun loadSharedHabits() {
        habitRepository.fetchSharedHabits().onEach { result ->
            when(result) {
                is ResultWrapper.Loading -> {
                    uiState.value = uiState.value.copy(isLoading = true)
                }
                is ResultWrapper.Success -> {
                    uiState.value = uiState.value.copy(
                        sharedHabits = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is ResultWrapper.Error -> {
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    // -----------------------------
    // 3) Adicionar Hábito
    // -----------------------------
    fun addHabit(nome: String, descricao: String) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if(userId != null) {
                val habit = Habit(
                    nome = nome,
                    descricao = descricao,
                    criadoPor = userId
                )
                habitRepository.addHabit(habit).onEach { result ->
                    when(result) {
                        is ResultWrapper.Success -> loadMyHabits()
                        is ResultWrapper.Loading -> uiState.value = uiState.value.copy(isLoading = true)
                        is ResultWrapper.Error -> uiState.value = uiState.value.copy(isLoading = false, error = result.message)
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    // -----------------------------
    // 4) Atualizar Hábito
    // -----------------------------
    fun updateHabit(id: String, nome: String, descricao: String) {
        viewModelScope.launch {
            habitRepository.updateHabit(id, nome, descricao).onEach { result ->
                when(result) {
                    is ResultWrapper.Success -> loadMyHabits()
                    is ResultWrapper.Loading -> uiState.value = uiState.value.copy(isLoading = true)
                    is ResultWrapper.Error -> uiState.value = uiState.value.copy(isLoading = false, error = result.message)
                }
            }.launchIn(viewModelScope)
        }
    }

    // -----------------------------
    // 5) Apagar Hábito
    // -----------------------------
    fun deleteHabit(id: String) {
        viewModelScope.launch {
            habitRepository.deleteHabit(id).onEach { result ->
                when(result) {
                    is ResultWrapper.Success -> loadMyHabits()
                    is ResultWrapper.Loading -> uiState.value = uiState.value.copy(isLoading = true)
                    is ResultWrapper.Error -> uiState.value = uiState.value.copy(isLoading = false, error = result.message)
                }
            }.launchIn(viewModelScope)
        }
    }

    // -----------------------------
    // 6) Partilhar Hábito
    // -----------------------------
    fun shareHabit(id: String, email: String) {
        viewModelScope.launch {
            habitRepository.shareHabit(id, email).onEach { result ->
                when(result) {
                    is ResultWrapper.Success -> loadMyHabits()
                    is ResultWrapper.Loading -> uiState.value = uiState.value.copy(isLoading = true)
                    is ResultWrapper.Error -> uiState.value = uiState.value.copy(isLoading = false, error = result.message)
                }
            }.launchIn(viewModelScope)
        }
    }
}
