package ipca.example.webapi.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ipca.example.webapi.viewmodel.TodoListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListView(viewModel: TodoListViewModel) {
    val todos by viewModel.todos.collectAsState()
    val selected by viewModel.selectedTodo.collectAsState()
    // Observar o estado de favoritos (Assumindo que adicionou a lógica no ViewModel)
    val favoriteTodoIds by viewModel.favoriteTodoIds.collectAsState()

    Box(modifier = Modifier.padding()) {

        //  Verificar se selected não é nulo
        if (selected != null) {
            val todoSelecionado = selected!!

            // Lógica de favoritos
            val isFavorite = todoSelecionado.id?.let { favoriteTodoIds.contains(it) } ?: false

            // Mostrar detalhes da tarefa selecionada
            TodoDetailView(
                todo = todoSelecionado, // O 'todoSelecionado' é garantidamente não-nulo (Todo)
                isFavorite = isFavorite,
                onBack =  {
                    viewModel.selectTodo(null) //  Voltar à lista
                },
                onFavoriteToggle = { todoId ->
                    viewModel.toggleFavorite(todoId)
                }
            )
        } else {
            // Mostrar lista de tarefas
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(todos) { todo ->
                    TodoViewCell(todo) {
                        viewModel.selectTodo(todo)
                    }
                }
            }
        }
    }
}