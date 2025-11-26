package ipca.example.webapi.todo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipca.example.webapi.models.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL
import java.lang.Exception

// Define a classe de estado
data class TodoListState(
    val todos: List<Todo> = emptyList(),
    val selectedTodo: Todo? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    // Adicionado: Conjunto de IDs de tarefas favoritas
    val favoriteTodoIds: Set<Int> = emptySet()
)

class TodoDBListViewModel : ViewModel() {

    // Gere o estado reativo da UI
    var uiState = mutableStateOf(TodoListState())
        private set

    init {
        fetchTodos()
    }

    private fun fetchTodos() {
        // Define o estado de carregamento como true
        uiState.value = uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = URL("https://dummyjson.com/todos").readText()
                val todosArray = JSONObject(response).getJSONArray("todos")
                val list = mutableListOf<Todo>()
                for (i in 0 until todosArray.length()) {
                    val item = todosArray.getJSONObject(i)
                    list.add(
                        Todo(
                            id = item.getInt("id"),
                            title = item.getString("todo"),
                            completed = item.getBoolean("completed"),
                            userId = item.getInt("userId")
                        )
                    )
                }
                // Atualiza o estado com a lista de tarefas e isLoading = false
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    todos = list
                )
            } catch (e: Exception) {
                // Atualiza o estado com o erro e isLoading = false
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    error = "Erro ao carregar tarefas: ${e.message}"
                )
            }
        }
    }

    // Função para selecionar/desselecionar uma tarefa, atualizando o uiState
    fun selectTodo(todo: Todo?) {
        uiState.value = uiState.value.copy(selectedTodo = todo)
    }

    // Função para adicionar/remover um todas dos favoritos
    fun toggleFavorite(todoId: Int) {
        val currentFavorites = uiState.value.favoriteTodoIds.toMutableSet()
        if (currentFavorites.contains(todoId)) {
            currentFavorites.remove(todoId) // Remover
        } else {
            currentFavorites.add(todoId) // Adicionar
        }

        uiState.value = uiState.value.copy(
            favoriteTodoIds = currentFavorites
        )
    }


    fun getFavoriteTodos(): List<Todo> {
        return uiState.value.todos.filter { todo ->
            todo.id?.let { uiState.value.favoriteTodoIds.contains(it) } ?: false
        }
    }
}