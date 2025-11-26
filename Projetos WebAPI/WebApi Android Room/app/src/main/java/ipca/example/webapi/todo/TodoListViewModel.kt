package ipca.example.webapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipca.example.webapi.models.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

class TodoListViewModel : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos

    private val _selectedTodo = MutableStateFlow<Todo?>(null)
    val selectedTodo: StateFlow<Todo?> = _selectedTodo

    //Estado para IDs de favoritos
    private val _favoriteTodoIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteTodoIds: StateFlow<Set<Int>> = _favoriteTodoIds // Expor o estado

    init {
        fetchTodos()
    }

    private fun fetchTodos() {
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
                viewModelScope.launch(Dispatchers.Main) {
                    _todos.value = list
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun selectTodo(todo: Todo?) {
        _selectedTodo.value = todo
    }

    // Função para adicionar/remover um favoritos
    fun toggleFavorite(todoId: Int) {
        val currentFavorites = _favoriteTodoIds.value.toMutableSet()
        if (currentFavorites.contains(todoId)) {
            currentFavorites.remove(todoId) // Remover
        } else {
            currentFavorites.add(todoId) // Adicionar
        }
        _favoriteTodoIds.value = currentFavorites.toSet()
    }
}
